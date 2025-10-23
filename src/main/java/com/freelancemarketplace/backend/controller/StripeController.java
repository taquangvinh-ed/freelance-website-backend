package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.service.PaymentService;
import com.freelancemarketplace.backend.service.imp.StripeService;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/stripe")
@AllArgsConstructor
public class StripeController {

    private StripeService stripeService;
    private PaymentService paymentService;


    private final Map<String, String> testIds = new HashMap<>();


    @GetMapping("/onboard/freelancer")
    public ResponseEntity<StripeService.OnboardingResponse> onboardFreelancer(
            @RequestParam String email, @RequestParam String country) throws StripeException {

        StripeService.OnboardingResponse response = stripeService.createFreelancerAccount(email, country);

        // Lưu ID để sử dụng cho bước thanh toán
        testIds.put("freelancerId", response.accountId);

        return ResponseEntity.ok(response);
    }

    /**
     * Tạo Customer (Client) và trả về ID.
     * http://localhost:8080/api/test/stripe/create/client
     */
    @GetMapping("/create/client")
    public ResponseEntity<String> createClient() throws StripeException {

        String email = "test_client_" + System.currentTimeMillis() + "@example.com";
        String customerId = stripeService.createTestClientCustomer(email, "Test Client");

        // Lưu ID để sử dụng cho bước thanh toán
        testIds.put("clientId", customerId);

        return ResponseEntity.ok("Client created with Customer ID: " + customerId);
    }

    // --- B. GIAO DỊCH (Thanh toán/Giải ngân/Hoàn tiền) -----------------------

    /**
     * Client thanh toán (Ký quỹ) cho một Milestone.
     * Cần chạy /create/client trước.
     * http://localhost:8080/api/test/stripe/escrow?amount=1000000
     */
//    @PostMapping("/escrow")
//    public ResponseEntity<String> createEscrow(@RequestParam BigDecimal amount) throws Exception {
//
//        if (!testIds.containsKey("clientId")) {
//            return ResponseEntity.badRequest().body("Vui lòng tạo Client trước (/create/client).");
//        }
//
//        // 1. Chuẩn bị DTO giả lập
//        ClientDTO clientDTO = new ClientDTO();
//        clientDTO.setStripeCustomerId(testIds.get("clientId")); // Lấy ID đã lưu
//
//        MileStoneDTO milestoneDTO = new MileStoneDTO();
//        milestoneDTO.setAmount(amount); // Số tiền của dự án
//        milestoneDTO.setDescription("Thiết kế logo mới");
//
//        // 2. Tạo Payment Intent (Escrow)
//        String intentId = paymentService.createEscrowPayment(milestoneDTO, clientDTO);
//
//        // Lưu ID Intent
//        testIds.put("paymentIntentId", intentId);
//
//        return ResponseEntity.ok("Payment Intent created (Escrow): " + intentId
//                + ". Total charged: " + amount.multiply(BigDecimal.valueOf(1.03)) + " VND (bao gồm 3% phí client)");
//    }

    /**
     * Giải ngân tiền cho Freelancer sau khi dự án hoàn thành.
     * Cần chạy /onboard/freelancer và /escrow trước.
     * http://localhost:8080/api/test/stripe/release
     */
//    @PostMapping("/release")
//    public ResponseEntity<String> releasePayment() throws Exception {
//
//        if (!testIds.containsKey("paymentIntentId") || !testIds.containsKey("freelancerId")) {
//            return ResponseEntity.badRequest().body("Vui lòng tạo Freelancer và Escrow trước.");
//        }
//
//        // 1. Chuẩn bị DTO giả lập (Lấy lại dữ liệu từ Escrow, ở đây dùng tạm)
//        MileStoneDTO milestoneDTO = new MileStoneDTO();
//        milestoneDTO.setAmount(BigDecimal.valueOf(1000000)); // Lấy lại số tiền ban đầu
//        milestoneDTO.setId("1");
//
//        // 2. Gọi Service giải ngân (Capture + Transfer)
//        paymentService.releasePayment(
//                testIds.get("paymentIntentId"),
//                milestoneDTO,
//                testIds.get("freelancerId")
//        );
//
//        return ResponseEntity.ok("Payment released. Transfered 90% to Freelancer "
//                + testIds.get("freelancerId") + ". Platform kept 10% fee.");
//    }

    /**
     * Hủy Escrow (trả lại tiền cho Client).
     * http://localhost:8080/api/test/stripe/refund
     */
    @PostMapping("/refund")
    public ResponseEntity<String> refundPayment() throws Exception {
        if (!testIds.containsKey("paymentIntentId")) {
            return ResponseEntity.badRequest().body("Vui lòng tạo Escrow trước.");
        }

        paymentService.refundPayment(testIds.get("paymentIntentId"));

        return ResponseEntity.ok("Payment Intent " + testIds.get("paymentIntentId") + " refunded/cancelled.");
    }


    @GetMapping("/onboarding/refresh")
    public String testOnboarding(@RequestParam(required = false) String stripe_code) {
        return "Onboarding Success! Code: " + stripe_code;
    }

}
