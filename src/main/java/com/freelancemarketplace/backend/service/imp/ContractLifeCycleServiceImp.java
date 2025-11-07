package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.service.ContractLifeCycleService;
import com.freelancemarketplace.backend.service.ContractReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAdjuster;
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


    @Override
    public void startWeeklyReporting(Long contractId) {
        if(scheduledReports.containsKey(contractId)){
            log.warn("Reporting for contract " + contractId + " is scheduled.");
            return;
        }

        Instant nextFriday5PM = Instant.now()
                .atZone(contractReportingService.getZoneId())
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY))
                .withHour(17).withMinute(0).withSecond(0)
                .toInstant();

        Runnable reportTask = ()->{
            log.info("Active automatically report for contract: {}", contractId);
            contractReportingService.generateWeeklyReport(contractId);
        };

        ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(reportTask, nextFriday5PM, Duration.ofDays(7*24*60*60*1000));
        scheduledReports.put(contractId, future);
        log.info("Report schedular for contract {} is started. The initial run at: {}", contractId, nextFriday5PM);

    }

    @Override
    public void stopWeeklyReporting(Long contractId) {
        ScheduledFuture<?> future = scheduledReports.remove(contractId);
        if(future != null){
            future.cancel(true);
            log.info("Lập lịch báo cáo hàng tuần cho Hợp đồng ID {} đã bị HỦY.", contractId);
        }else{
            log.warn("Không tìm thấy lập lịch đang chạy cho Hợp đồng ID {}.", contractId);
        }
        log.info("Bắt đầu tạo báo cáo CUỐI CÙNG cho Hợp đồng ID {}.", contractId);
        contractReportingService.generateWeeklyReport(contractId);
    }
}
