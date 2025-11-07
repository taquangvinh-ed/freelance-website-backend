package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.WeeklyReportDTO;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractReportingServiceImp implements ContractReportingService {

    private final ContractsRepository contractsRepository;
    private final TimeLogRepository timeLogRepository;
    private final WeeklyReportModelRepository weeklyReportModelRepository;
    private final WeeklyReportItemModelRepository weeklyReportItemModelRepository;
    private final WeeklyReportMapper weeklyReportMapper;
    @Override
    public WeeklyReportDTO generateWeeklyReport(Long contractId, Instant startTime, Instant endTime) {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                ()-> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        if(contract.getTypes() != ContractTypes.HOURLY){
            log.warn("Hợp đồng ID {} không phải Hourly, bỏ qua báo cáo.", contractId);
            return null;
        }


        ZoneId zoneId = getZoneId();

        List<TimeLog> completedLogs = timeLogRepository.findByContractAndStatusAndEndTimeBetween(contract, TimeLogStatus.COMPLETED, startTime, endTime);

        if (completedLogs.isEmpty()) {
            log.info("Không có TimeLog hoàn thành nào cho Contract ID: {} trong phạm vi báo cáo.", contractId);
            return null;
        }
        double totalHours = completedLogs.stream().mapToDouble(log -> ChronoUnit.MILLIS.between(log.getStartTime(), log.getEndTime())/3600000.0).sum();
        List<WeeklyReportItemModel> taskList = completedLogs.stream().map(log->{
            WeeklyReportItemModel item = new WeeklyReportItemModel();
            item.setStart(log.getStartTime());
            item.setEnd(log.getEndTime());
            item.setDescription(log.getDescription());
            return item;
        }).toList();
        List<WeeklyReportItemModel> savedTaskList = weeklyReportItemModelRepository.saveAll(taskList);

        WeeklyReportModel newWeeklyReport = new WeeklyReportModel();
        newWeeklyReport.setItems(savedTaskList);
        newWeeklyReport.setTotalHours(totalHours);
        WeeklyReportModel savedReport = weeklyReportModelRepository.save(newWeeklyReport);
        return weeklyReportMapper.toDto(savedReport);
    }

    @Override
    public ZoneId getZoneId(){
        return ZoneId.systemDefault();
    }

}
