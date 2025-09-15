    package com.freelancemarketplace.backend.dto;


    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    public class AdminDTO {
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String phoneNumber;
        private String title;
    }
