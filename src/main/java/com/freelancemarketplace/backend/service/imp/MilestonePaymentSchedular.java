package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.MileStoneModel;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MilestonePaymentSchedular {

    private final ContractsRepository contractsRepository;
    private final PaymentService paymentService;
    private final MileStoneMapper mileStoneMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    public void releaseEscrowPayments() {
        List<ContractModel> contracts = contractsRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = calendar.getTime();

        for (ContractModel contract : contracts) {
            for (MileStoneModel milestone : contract.getMileStones()) {
                if (milestone.getStatus().equals(MileStoneStatus.COMPLETED) && milestone.getCompletedAt() != null) {
                    if (milestone.getCompletedAt().before(sevenDaysAgo)) {
                        try {
                            MileStoneDTO mileStoneDTO = mileStoneMapper.toDto(milestone);
                            paymentService.releasePayment(milestone.getPaymentIntentId(), mileStoneDTO, contract.getFreelancer().getStripeCustomerId());
                            milestone.setStatus(MileStoneStatus.RELEASED);
                            contractsRepository.save(contract);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to release payment for milestone with id: " + milestone.getMileStoneId() + ": " + e);
                        }
                    }

                }
            }
        }
    }

}
