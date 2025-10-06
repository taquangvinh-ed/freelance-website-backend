package com.freelancemarketplace.backend.enums;

public enum InteractionType {
    VIEWED,       // Đã xem trang dự án
    SAVED,        // Đã lưu (Bookmark)
    BIDDED,       // Đã gửi đề xuất (Proposal)
    WON,          // Đã thắng thầu (Tương tác tích cực nhất)
    IGNORED       // (Có thể suy luận, nếu đã xem nhưng không làm gì)
}
