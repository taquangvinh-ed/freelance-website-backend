package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.WeeklyReportDTO;
import com.freelancemarketplace.backend.dto.WeeklyReportItemDTO;
import com.freelancemarketplace.backend.mapper.WeeklyReportMapper;
import com.freelancemarketplace.backend.model.WeeklyReportItemModel;
import com.freelancemarketplace.backend.model.WeeklyReportModel;
import com.freelancemarketplace.backend.enums.ContractTypes;
import com.freelancemarketplace.backend.enums.TimeLogStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.TimeLog;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.TimeLogRepository;
import com.freelancemarketplace.backend.repository.WeeklyReportItemModelRepository;
import com.freelancemarketplace.backend.repository.WeeklyReportModelRepository;
import com.freelancemarketplace.backend.service.ContractReportingService;
import com.freelancemarketplace.backend.service.EmailService;
import com.freelancemarketplace.backend.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractReportingServiceImp implements ContractReportingService {

    private final ContractsRepository contractsRepository;
    private final TimeLogRepository timeLogRepository;
    private final WeeklyReportModelRepository weeklyReportModelRepository;
    private final WeeklyReportItemModelRepository weeklyReportItemModelRepository;
    private final WeeklyReportMapper weeklyReportMapper;
    private final EmailService emailService;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public WeeklyReportDTO generateWeeklyReport(Long contractId, Instant startTime, Instant endTime) {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                ()-> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        if(contract.getTypes() != ContractTypes.HOURLY){
            log.warn("Hợp đồng ID {} không phải Hourly, bỏ qua báo cáo.", contractId);
            return null;
        }

        List<TimeLog> completedLogs = timeLogRepository.findByContractAndStatusAndEndTimeBetween(contract, TimeLogStatus.COMPLETED, startTime, endTime);

        if (completedLogs.isEmpty()) {
            log.info("Không có TimeLog hoàn thành nào cho Contract ID: {} trong phạm vi báo cáo.", contractId);
            return null;
        }
        double totalHours = completedLogs.stream().mapToDouble(log -> ChronoUnit.MILLIS.between(log.getStartTime(), log.getEndTime())/3600000.0).sum();

        WeeklyReportModel newWeeklyReport = new WeeklyReportModel();
        newWeeklyReport.setTotalHours(Math.round(totalHours * 100.0) / 100.0);
        newWeeklyReport.setContract(contract);
        WeeklyReportModel savedReport = weeklyReportModelRepository.save(newWeeklyReport);

        List<WeeklyReportItemModel> taskList = completedLogs.stream().map(log->{
            WeeklyReportItemModel item = new WeeklyReportItemModel();
            item.setStartTime(log.getStartTime());
            item.setEndTime(log.getEndTime());
            item.setDescription(log.getDescription());
            item.setWeeklyReport(savedReport);
            return item;
        }).toList();
        List<WeeklyReportItemModel> savedTaskList = weeklyReportItemModelRepository.saveAll(taskList);

        savedReport.setItems(savedTaskList);
        WeeklyReportModel reportWithItems = weeklyReportModelRepository.save(savedReport);

        try {
            paymentService.createHourlyPaymentFromWeeklyReport(contract, reportWithItems);
        } catch (Exception e) {
            log.error("Không thể tạo hourly payment từ weekly report. contractId={}, reportId={}",
                    contractId, reportWithItems.getWeeklyReportId(), e);
        }

        sendWeeklySummaryEmail(contract, reportWithItems, startTime, endTime);

        WeeklyReportDTO dto = weeklyReportMapper.toDto(reportWithItems);
        dto.setLabel(formatWeekLabel(startTime, endTime));
        dto.setSortKey(startTime.toString());
        return dto;
    }

    @Override
    public ZoneId getZoneId(){
        return ZoneId.systemDefault();
    }


    @Override
    public List<WeeklyReportDTO> getAllLogs(Long contractId){
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                ()-> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        // 1. Xử lý trường hợp không phải HOURLY: Trả về danh sách rỗng (List.of())
        if(contract.getTypes() != ContractTypes.HOURLY){
            log.warn("Hợp đồng ID {} không phải Hourly, bỏ qua báo cáo.", contractId);
            return List.of();
        }

        ZoneId zoneId = ZoneId.systemDefault();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        Instant startTime = contract.getStartDate().toInstant();
        log.info("Start time: {}", startTime);
        Instant endTime = Instant.now();
        log.info("End time: {}", endTime);
        List<TimeLog> completedLogs = timeLogRepository.findByContractAndStatusAndEndTimeBetween(
                contract, TimeLogStatus.COMPLETED, startTime, endTime
        );

        // 3. Xử lý trường hợp không có Logs: Trả về danh sách rỗng (List.of())
        if (completedLogs.isEmpty()) {
            log.info("Không có TimeLog hoàn thành nào cho Contract ID: {} trong phạm vi báo cáo.", contractId);
            return List.of();
        }

        // 4. Sắp xếp Log trong mỗi tuần (Mới nhất lên trên)
        List<TimeLog> sortedLogs = completedLogs.stream()
                .sorted(Comparator.comparing(TimeLog::getEndTime).reversed())
                .collect(Collectors.toList()); // Dùng collect(Collectors.toList()) để tương thích

        // 5. Nhóm theo Tuần (Key là Year-Week)
        Map<String, List<TimeLog>> groupedByWeek = sortedLogs.stream().collect(Collectors.groupingBy(log->{
            // log.getEndTime() đã là Instant, dùng trực tiếp
            LocalDateTime localDateTime = LocalDateTime.ofInstant(log.getEndTime(), zoneId);
            int year = localDateTime.getYear();
            int weekOfYear = localDateTime.get(weekFields.weekOfWeekBasedYear());
            return String.format("%d-%02d", year, weekOfYear);
        }));

        List<WeeklyReportDTO> logs = groupedByWeek.entrySet().stream().map(entry -> {
                    WeeklyReportDTO weeklyReportDTO = new WeeklyReportDTO();

                    // Chuyển đổi Item Logs
                    List<WeeklyReportItemDTO> items = entry.getValue().stream().map(timeLog->{
                        WeeklyReportItemDTO newlog = new WeeklyReportItemDTO();
                        newlog.setStartTime(timeLog.getStartTime());
                        newlog.setEndTime(timeLog.getEndTime());
                        newlog.setDescription(timeLog.getDescription());
                        return newlog;
                    }).collect(Collectors.toList()); // Dùng collect(Collectors.toList())

                    weeklyReportDTO.setItems(items);

                    // Tính tổng giờ
                    double totalHours = entry.getValue().stream()
                            // ChronoUnit.MILLIS.between(start, end) trả về long
                            .mapToDouble(log -> ChronoUnit.MILLIS.between(log.getStartTime(), log.getEndTime()) / 3600000.0)
                            .sum();

                    // Làm tròn tổng giờ
                    weeklyReportDTO.setTotalHours(Math.round(totalHours * 100.0) / 100.0);
                    weeklyReportDTO.setLabel("Tuần: " + entry.getKey());

                    // Thêm key để sắp xếp chính xác hơn
                    weeklyReportDTO.setSortKey(entry.getKey());

                    return weeklyReportDTO;
                })
                // 6. Sắp xếp Tuần: Dùng key (Year-Week) để đảm bảo tuần mới nhất lên trên
                .sorted(Comparator.comparing(WeeklyReportDTO::getSortKey).reversed())
                .collect(Collectors.toList());

        return logs;
    }

    private void sendWeeklySummaryEmail(ContractModel contract, WeeklyReportModel report, Instant startTime, Instant endTime) {
        String to = resolveClientEmail(contract);
        if (to == null || to.isBlank()) {
            log.warn("Không tìm thấy email client cho Contract ID {}. Bỏ qua gửi weekly summary.", contract.getContractId());
            return;
        }

        String subject = "[FreelancerHub] Weekly work summary - Contract #" + contract.getContractId();
        String html = buildWeeklySummaryHtml(contract, report, startTime, endTime);

        try {
            emailService.sendHtmlEmail(to, subject, html);
            log.info("Đã gửi weekly summary email cho client {} (contractId={}, reportId={})",
                    to, contract.getContractId(), report.getWeeklyReportId());
        } catch (Exception e) {
            log.error("Gửi weekly summary email thất bại (contractId={}, reportId={})",
                    contract.getContractId(), report.getWeeklyReportId(), e);
        }
    }

    private String resolveClientEmail(ContractModel contract) {
        if (contract.getClient() == null) {
            return null;
        }

        if (contract.getClient().getUser() != null && contract.getClient().getUser().getEmail() != null) {
            return contract.getClient().getUser().getEmail();
        }

        return contract.getClient().getEmail();
    }

    private String formatWeekLabel(Instant startTime, Instant endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(getZoneId());
        return "Week " + formatter.format(startTime) + " -> " + formatter.format(endTime);
    }

    private String buildWeeklySummaryHtml(ContractModel contract, WeeklyReportModel report, Instant startTime, Instant endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(getZoneId());

        StringBuilder itemsHtml = new StringBuilder();
        for (WeeklyReportItemModel item : report.getItems()) {
            itemsHtml.append("<li><b>")
                    .append(formatter.format(item.getStartTime()))
                    .append(" - ")
                    .append(formatter.format(item.getEndTime()))
                    .append("</b>: ")
                    .append(item.getDescription() == null ? "No description" : item.getDescription())
                    .append("</li>");
        }

        return "<html><body>"
                + "<h3>Weekly summary</h3>"
                + "<p><b>Contract ID:</b> " + contract.getContractId() + "</p>"
                + "<p><b>Period:</b> " + formatWeekLabel(startTime, endTime) + "</p>"
                + "<p><b>Total hours:</b> " + report.getTotalHours() + "</p>"
                + "<p><b>Completed tasks:</b></p>"
                + "<ul>" + itemsHtml + "</ul>"
                + "</body></html>";
    }

}
