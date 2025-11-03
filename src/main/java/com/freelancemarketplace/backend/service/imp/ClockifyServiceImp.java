package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.config.ClockifyConfig;
import com.freelancemarketplace.backend.dto.ClockifyStartRequest;
import com.freelancemarketplace.backend.dto.ClockifyStopRequest;
import com.freelancemarketplace.backend.dto.ClockifyTimeEntryResponse;
import com.freelancemarketplace.backend.enums.TimeLogStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.TimeLog;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.TimeLogRepository;
import com.freelancemarketplace.backend.service.ClockifyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ClockifyServiceImp implements ClockifyService {

    private final ClockifyConfig config;
    private final RestTemplate restTemplate;
    private final FreelancersRepository freelancersRepository;
    private final ContractsRepository contractsRepository;
    private final TimeLogRepository timeLogRepository;


    @Override
    @Transactional
    public ClockifyTimeEntryResponse startTimeEntry(
            Long freelancerId,
            Long contractId,
            ClockifyStartRequest request) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                ()-> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        if (timeLogRepository.existsByFreelancerAndStatus(freelancer, TimeLogStatus.ACTIVE)) {
            throw new IllegalStateException("Freelancer already has an active timer running in contract " + contractId);
        }


        String freelancerClockifyApiKey = freelancer.getClockifyApiKey();
        String workspaceId = config.getDefaultWorkspaceId();

        Long  clockifyProjectId = contract.getContractProject().getProjectId();
        request.setProjectId(clockifyProjectId);

        request.setStart(Instant.now().toString());


        // 1. Chuẩn bị Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(config.getApiKeyHeaderName(), freelancerClockifyApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClockifyStartRequest> entity = new HttpEntity<>(request, headers);
        String url = config.getBaseUrl() + "/workspaces/" + workspaceId + "/time-entries";

        try {
            ResponseEntity<ClockifyTimeEntryResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, ClockifyTimeEntryResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ClockifyTimeEntryResponse clockifyResponse = response.getBody();

                // 4. ĐỒNG BỘ: Lưu trạng thái ACTIVE vào DB SAU KHI Clockify thành công
                TimeLog timeLog = new TimeLog();
                timeLog.setFreelancer(freelancer);
                timeLog.setContract(contract);

                // Lấy ID và Thời gian từ phản hồi Clockify
                timeLog.setClockifyEntryId(clockifyResponse.getId());
                timeLog.setStartTime(Instant.parse(clockifyResponse.getTimeInterval().getStart()));
                timeLog.setStatus(TimeLogStatus.ACTIVE);
                timeLogRepository.save(timeLog);

                return clockifyResponse;

            } else {
                throw new RuntimeException("Clockify returned unexpected status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // Xử lý lỗi API client (ví dụ: 401 Unauthorized, 400 Bad Request)
            throw new RuntimeException("Clockify API Error (Start): " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public ClockifyTimeEntryResponse stopTimeEntry(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        // 2. Tìm bản ghi TimeLog đang ACTIVE trong DB của bạn
        TimeLog activeTimeLog = timeLogRepository.findByFreelancerAndStatus(freelancer, TimeLogStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active timer found for this freelancer."));

        String freelancerClockifyApiKey = freelancer.getClockifyApiKey();
        String workspaceId = config.getDefaultWorkspaceId();
        String timeEntryId = activeTimeLog.getClockifyEntryId(); // 🎯 Lấy ID Clockify đã lưu

        // 3. Chuẩn bị và Gọi Clockify API (PATCH)
        ClockifyStopRequest request = new ClockifyStopRequest();
        request.setEnd(Instant.now().toString()); // Dừng timer ngay bây giờ

        HttpHeaders headers = new HttpHeaders();
        headers.set(config.getApiKeyHeaderName(), freelancerClockifyApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClockifyStopRequest> entity = new HttpEntity<>(request, headers);

        String url = config.getBaseUrl() + "/workspaces/" + workspaceId + "/time-entries/" + timeEntryId;

        try {
            ResponseEntity<ClockifyTimeEntryResponse> response = restTemplate.exchange(
                    url, HttpMethod.PATCH, entity, ClockifyTimeEntryResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ClockifyTimeEntryResponse clockifyResponse = response.getBody();

                // 4. ĐỒNG BỘ: Cập nhật trạng thái TimeLog thành COMPLETED
                activeTimeLog.setEndTime(Instant.parse(clockifyResponse.getTimeInterval().getEnd()));
                activeTimeLog.setStatus(TimeLogStatus.COMPLETED);
                timeLogRepository.save(activeTimeLog);

                return clockifyResponse;

            } else {
                throw new RuntimeException("Clockify returned unexpected status (Stop): " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // Xử lý lỗi API client
            throw new RuntimeException("Clockify API Error (Stop): " + e.getResponseBodyAsString(), e);
        }
    }

}
