package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.config.TogglConfig;
import com.freelancemarketplace.backend.dto.TogglStartRequestDTO;
import com.freelancemarketplace.backend.dto.TogglTimeEntryResponseDTO;
import com.freelancemarketplace.backend.dto.TogglUserRequestDTO;
import com.freelancemarketplace.backend.dto.TogglUserResponseDTO;
import com.freelancemarketplace.backend.enums.TimeLogStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.TimeLog;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.TimeLogRepository;
import com.freelancemarketplace.backend.service.TogglService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TogglServiceImp implements TogglService {

    private final TogglConfig config;
    private final RestTemplate restTemplate;
    private final FreelancersRepository freelancersRepository;
    private final ContractsRepository contractsRepository;
    private final TimeLogRepository timeLogRepository;

    private HttpHeaders createAuthHeaders() {
        String authString = config.getApiKey() + ":api_token";

        // 2. Mã hóa sang Base64
        String base64Credentials = new String(java.util.Base64.getEncoder().encode(authString.getBytes()));

        // 3. Thiết lập Headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }


    @Override
    public String getOrCreateFreelancerUser(Long freelancerId, String freelancerName, String email) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found: " + freelancerId));

        if (freelancer.getTogglUserId() != null && !freelancer.getTogglUserId().isEmpty()) {
            return freelancer.getTogglUserId();
        }

        String orgId = config.getOrganizationId();
        if (orgId == null || orgId.isBlank()) {
            throw new IllegalStateException("Toggl organization ID is not configured");
        }

        String url = config.getBaseUrl() + "/organizations/" + orgId + "/users";
        HttpHeaders headers = createAuthHeaders();
        TogglUserRequestDTO requestBody = new TogglUserRequestDTO(email, freelancerName);
        HttpEntity<TogglUserRequestDTO> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TogglUserResponseDTO> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, TogglUserResponseDTO.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                String newTogglId = response.getBody().getId().toString();

                freelancer.setTogglUserId(newTogglId);  // ĐÃ SỬA
                freelancersRepository.save(freelancer);

                return newTogglId;
            }
            throw new RuntimeException("Toggl create user failed: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Toggl API Error (Create User): " + e.getResponseBodyAsString(), e);
        }
    }



    @Override
    public Long createProjectOnToggl(String projectName) {
        String url = config.getBaseUrl() + "/workspaces/" + config.getDefaultWorkspaceId() + "/projects";

        Map<String, Object> body = new HashMap<>();
        body.put("name", projectName);
        body.put("active", true);
        body.put("billable", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, createAuthHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // **BƯỚC SỬA LỖI QUAN TRỌNG:** Ánh xạ trực tiếp từ body Toggl V9
                Map<String, Object> togglProjectResponse = response.getBody();

                // Lấy ID trực tiếp từ cấp cao nhất của phản hồi
                Object projectIdObject = togglProjectResponse.get("id");

                if (projectIdObject instanceof Number) {


                    return ((Number) projectIdObject).longValue();
                } else {
                    // Xử lý trường hợp ID bị thiếu hoặc không phải số
                    throw new RuntimeException("Toggl project creation failed: ID is missing or malformed.");
                }
            }
            throw new RuntimeException("Toggl create project failed: " + response.getStatusCode());

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Toggl API Error (Create Project): " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Toggl project: " + e.getMessage(), e);
        }
    }


    @Override
    @Transactional
    public TogglTimeEntryResponseDTO startTimeEntry( // Trả về DTO phản hồi Toggl
                                                     Long freelancerId,
                                                     Long contractId,
                                                     String description) { // Giữ nguyên tham số


        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );


        Long togglProjectId;
        if(contract.getToggleProjectId() == null){
            String projectName = contract.getContractProject().getTitle();
            togglProjectId = createProjectOnToggl(projectName);
            contract.setToggleProjectId(togglProjectId);
            contractsRepository.save(contract);
        }
        else{
            togglProjectId = contract.getToggleProjectId();
        }

    log.info("toggl id: {}",togglProjectId);
        if (timeLogRepository.existsByFreelancerAndStatus(freelancer, TimeLogStatus.ACTIVE)) {
            throw new IllegalStateException("Freelancer already has an active timer running in contract " + contractId);
        }

        String freelancerName = freelancer.getFirstName() + " " + freelancer.getLastName();
        String workspaceId = config.getDefaultWorkspaceId();

        TogglStartRequestDTO request = new TogglStartRequestDTO();
        String startTime = Instant.now().toString();
        request.setWorkspace_id(Long.parseLong(workspaceId));
        request.setProject_id(togglProjectId);
        request.setStart(startTime);
        request.setDescription(description + "| Freelancer: " + freelancerName);
        request.setBillable(true);
        request.setCreated_with("FreelanceMVP_App");
        request.setDuration(-1L);

        HttpHeaders headers = createAuthHeaders(); // <-- Sử dụng Basic Auth Headers

        HttpEntity<TogglStartRequestDTO> entity = new HttpEntity<>(request, headers);

        String url = config.getBaseUrl() + "/workspaces/" + workspaceId + "/time_entries";

        try {
            ResponseEntity<TogglTimeEntryResponseDTO> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, TogglTimeEntryResponseDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                TogglTimeEntryResponseDTO togglResponse = response.getBody();

                // 5. ĐỒNG BỘ DB NỘI BỘ
                TimeLog timeLog = new TimeLog();
                timeLog.setFreelancer(freelancer);
                timeLog.setContract(contract);

                // Lấy ID và Thời gian từ phản hồi Toggl
                timeLog.setTogglEntryId(togglResponse.getId().toString());
                timeLog.setDescription(togglResponse.getDescription());
                // Toggl trả về startTime dưới trường "start" (hoặc "at") trong response body
                timeLog.setStartTime(Instant.parse(togglResponse.getStart()));
                timeLog.setStatus(TimeLogStatus.ACTIVE);
                timeLogRepository.save(timeLog);

                return togglResponse;

            } else {
                throw new RuntimeException("Toggl returned unexpected status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // Xử lý lỗi API client (ví dụ: 401 Unauthorized, 400 Bad Request)
            throw new RuntimeException("Toggl API Error (Start): " + e.getResponseBodyAsString(), e);
        }
    }

    @Transactional
    @Override
    public TogglTimeEntryResponseDTO stopTimeEntry(Long freelancerId, Long contractId) { // Đã đổi kiểu trả về
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        // 1. Tìm bản ghi TimeLog đang ACTIVE trong DB nội bộ
        List<TimeLog> activeTimeLog = timeLogRepository.findByFreelancerAndStatus(freelancer, TimeLogStatus.ACTIVE);

        Optional<TimeLog> timelog = Optional.ofNullable(activeTimeLog.stream().filter(log -> log.getContract().getContractId().equals(contractId)).findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No active timer found for Contract ID: " + contractId + " for this freelancer."
                )));

        String workspaceId = config.getDefaultWorkspaceId();
        String timeEntryId = timelog.get().getTogglEntryId(); // Giữ nguyên trường DB để chứa Toggl ID

        // 2. Chuẩn bị Header (Basic Auth)
        HttpHeaders headers = createAuthHeaders(); // <-- Sử dụng Basic Auth Headers

        // 3. Chuẩn bị và Gọi Toggl API (PATCH)
        // Lưu ý: Toggl chỉ cần PATCH (cập nhật) mục Time Entry, không cần gửi Body rỗng

        // Endpoint Toggl: PATCH /workspaces/{id}/time_entries/{id}/stop (Thêm /stop)
        String url = String.format("%s/workspaces/%s/time_entries/%s/stop",
                config.getBaseUrl(), workspaceId, timeEntryId);

        try {
            // Không cần gửi body cho endpoint /stop của Toggl
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<TogglTimeEntryResponseDTO> response = restTemplate.exchange(
                    url, HttpMethod.PATCH, entity, TogglTimeEntryResponseDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                TogglTimeEntryResponseDTO togglResponse = response.getBody();

                // 4. ĐỒNG BỘ DB NỘI BỘ
                // Toggl trả về thời gian kết thúc trong trường 'stop' (hoặc 'at') của phản hồi

                timelog.get().setEndTime(Instant.parse(togglResponse.getStop()));
                timelog.get().setStatus(TimeLogStatus.COMPLETED);
                timeLogRepository.save(timelog.get());

                return togglResponse;

            } else {
                throw new RuntimeException("Toggl returned unexpected status (Stop): " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // Xử lý lỗi API client
            throw new RuntimeException("Toggl API Error (Stop): " + e.getResponseBodyAsString(), e);
        }
    }

    @Transactional
    public void generateWeeklyReport(Long contractId){
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);

        ContractModel contractModel = contractsRepository.findById(contractId).orElseThrow(
                ()->new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );
        generateReportForContract(contractModel, weekStart, weekEnd);
    }

    private void generateReportForContract(ContractModel contract, LocalDate weekStart, LocalDate weekEnd){
        HttpHeaders headers = createAuthHeaders();

    }

}
