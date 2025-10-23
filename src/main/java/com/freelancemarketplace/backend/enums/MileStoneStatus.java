package com.freelancemarketplace.backend.enums;

public enum MileStoneStatus {
    PENDING,      // Milestone được đề xuất, chưa được xem xét
    ACCEPTED,     // Khách hàng đồng ý với milestone
    APPROVED,     // Milestone được phê duyệt trong hợp đồng (đã nạp tiền vào escrow)
    COMPLETED,
    ESCROWED,// Freelancer hoàn thành milestone
    RELEASED,     // Tiền của milestone được giải phóng
    DISPUTED      // Có tranh chấp liên quan đến milestone
 }
