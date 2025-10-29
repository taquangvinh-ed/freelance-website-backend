package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.ContractTypes;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.service.ProgressCalculationService;
import org.springframework.stereotype.Service;

@Service
public class ProgressCalculationServiceImp implements ProgressCalculationService {


    @Override
    public  double calculateProgress(ContractModel contract) {
        if (contract.getStatus() != ContractStatus.ACTIVE) {
            return contract.getStatus() == ContractStatus.COMPLETED ? 100.0 : 0.0;
        }

        if (contract.getTypes() == ContractTypes.FIXED_PRICE) {
            return calculateFixedPriceProgress(contract);
        } else if (contract.getTypes() == ContractTypes.HOURLY) {
            return calculateHourlyProgress(contract);
        }
        return 0.0;
    }

    public double calculateFixedPriceProgress(ContractModel contract) {
        if (contract.getMileStones().isEmpty()) return 0.0;

        long totalMilestones = contract.getMileStones().size();

        // Tiến độ tính bằng số lượng Milestones ĐÃ HOÀN THÀNH (RELEASED/PAID)
        long completedMilestones = contract.getMileStones().stream()
                .filter(ms -> ms.getStatus() == MileStoneStatus.COMPLETED)
                .count();

        return (completedMilestones / (double) totalMilestones) * 100.0;
    }

    public double calculateHourlyProgress(ContractModel contract) {
//        // Đối với hợp đồng HOURLY, tiến độ thường được đo bằng TỶ LỆ GIỜ ĐÃ LÀM
//        // trên Tổng số giờ ước tính trong hợp đồng.
//
//        // Giả định ContractModel có trường estimatedHours
//        Double estimatedHours = contract.getEstimatedHours();
//        if (estimatedHours == null || estimatedHours == 0) return 0.0;
//
//        // Giả định bạn có TimeSheetModel với trường totalHours
//        Double totalLoggedHours = contract.getTimeSheets().stream()
//                .filter(ts -> ts.getStatus() == TimeSheetStatus.APPROVED || ts.getStatus() == TimeSheetStatus.PAID)
//                .mapToDouble(TimeSheetModel::getTotalHours)
//                .sum();
//
//        // Giới hạn tiến độ ở mức 100%
//        return Math.min(100.0, (totalLoggedHours / estimatedHours) * 100.0);
        return 0.0;
    }

}
