package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.WeeklyReportItemDTO;
import com.freelancemarketplace.backend.dto.WeeklyReportResponseDTO;
import com.freelancemarketplace.backend.enums.ContractTypes;
import com.freelancemarketplace.backend.enums.TimeLogStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.TimeLog;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.TimeLogRepository;
import com.freelancemarketplace.backend.service.ContractReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractReportingServiceImp implements ContractReportingService {

    private final ContractsRepository contractsRepository;
    private final TimeLogRepository timeLogRepository;

    @Override
    public WeeklyReportResponseDTO generateWeeklyReport(Long contractId) {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                ()-> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        if(contract.getTypes() != ContractTypes.HOURLY){
            log.warn("Hợp đồng ID {} không phải Hourly, bỏ qua báo cáo.", contractId);
            return new WeeklyReportResponseDTO(Collections.emptyList(), 0.0);
        }


        ZoneId zoneId = getZoneId();

        // Thời điểm kết thúc báo cáo (5:00 PM Thứ Sáu hiện tại)
        Instant endOfReport = ZonedDateTime.now(zoneId).with(DayOfWeek.FRIDAY).truncatedTo(ChronoUnit.DAYS).withHour(17).toInstant();
        // Thời điểm bắt đầu báo cáo (00:00:00 Thứ Hai của tuần đó)
        Instant startOfReport = endOfReport.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);


        List<TimeLog> completedLogs = timeLogRepository.findByContractAndStatusAndEndTimeBetween(contract, TimeLogStatus.COMPLETED, startOfReport, endOfReport);

        if (completedLogs.isEmpty()) {
            log.info("Không có TimeLog hoàn thành nào cho Contract ID: {} trong phạm vi báo cáo.", contractId);
            return new WeeklyReportResponseDTO(Collections.emptyList(), 0.0);
        }
        double totalHours = completedLogs.stream().mapToDouble(log -> ChronoUnit.MILLIS.between(log.getStartTime(), log.getEndTime())/3600000.0).sum();
        List<WeeklyReportItemDTO> taskList = completedLogs.stream().map(log->{
            WeeklyReportItemDTO item = new WeeklyReportItemDTO();
            item.setStart(log.getStartTime());
            item.setEnd(log.getEndTime());
            item.setDescription(log.getDescription());
            return item;
        }).toList();

        WeeklyReportResponseDTO response = new WeeklyReportResponseDTO();
        response.setItem(taskList);
        response.setTotalHours(totalHours);
        return response;
    }
    @Override
    public ZoneId getZoneId(){
        return ZoneId.systemDefault();
    }

}
