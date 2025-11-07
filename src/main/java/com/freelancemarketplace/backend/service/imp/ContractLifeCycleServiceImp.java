package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.service.ContractLifeCycleService;
import com.freelancemarketplace.backend.service.ContractReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractLifeCycleServiceImp implements ContractLifeCycleService {

    private final TaskScheduler taskScheduler;
    private final ContractReportingService contractReportingService;
    private final Map<Long, ScheduledFuture<?>> scheduledReports = new ConcurrentHashMap<>();
    private final Map<Long, Instant> lastReportStartTime = new ConcurrentHashMap<>();

    @Override
    public void startWeeklyReporting(Long contractId) {
        if (scheduledReports.containsKey(contractId)) {
            log.warn("Reporting for contract " + contractId + " is scheduled.");
            return;
        }

        ZonedDateTime nextFriday5PM = Instant.now()
                .atZone(contractReportingService.getZoneId())
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY))
                .withHour(17).withMinute(0).withSecond(0)
                .withNano(0);


        ZonedDateTime nowZoned = Instant.now().atZone(contractReportingService.getZoneId());

        if(nowZoned.isAfter(nextFriday5PM)){
            nextFriday5PM = nextFriday5PM.plusWeeks(1);
        }

        Instant firstRunTime = nextFriday5PM.toInstant();
        lastReportStartTime.put(contractId,Instant.now());


        Runnable reportTask = () -> {
            log.info("Active automatically report for contract: {}", contractId);
            generateAndAdvanceReport(contractId);
        };
        ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(reportTask, firstRunTime, Duration.ofDays(7));
        scheduledReports.put(contractId, future);
        log.info("Report schedular for contract {} is started. The initial run at: {}", contractId, nextFriday5PM);

    }


    private void generateAndAdvanceReport(Long contractId) {
        Instant startTime = lastReportStartTime.get(contractId);
        if (startTime == null) {
            log.error("Lỗi Fatal: Không tìm thấy Start Time cho Hợp đồng ID {}. Hủy tác vụ.", contractId);
            stopWeeklyReporting(contractId);
            return;
        }

        Instant endTime = Instant.now();

        try {
            // 3. GỌI BÁO CÁO: Truyền Start Time và End Time ĐỘNG
            contractReportingService.generateWeeklyReport(contractId, startTime, endTime);

            // 4. CẬP NHẬT START TIME LÊN THỜI ĐIỂM KẾT THÚC CỦA CHU KỲ VỪA RỒI
            lastReportStartTime.put(contractId, endTime);
            log.info("Thành công: Báo cáo cho Hợp đồng ID {} đã chạy từ {} đến {}. Start Time mới được đặt là: {}",
                    contractId, startTime, endTime, endTime);

        } catch (Exception e) {
            log.error("Lỗi khi tạo báo cáo cho Hợp đồng ID {}. Không cập nhật Start Time.", contractId, e);
            // Không cập nhật Start Time để báo cáo thử lại tuần sau.
        }
    }

    @Override
    public void stopWeeklyReporting(Long contractId) {
        generateAndAdvanceReport(contractId);
        ScheduledFuture<?> future = scheduledReports.remove(contractId);
        if (future != null) {
            future.cancel(true);
            log.info("Lập lịch báo cáo hàng tuần cho Hợp đồng ID {} đã bị HỦY.", contractId);
        }else {
            log.warn("Không tìm thấy lập lịch đang chạy cho Hợp đồng ID {}.", contractId);
        }
        lastReportStartTime.remove(contractId);
    }
}
