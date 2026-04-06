package com.freelancemarketplace.backend.user.application.service;

import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.user.domain.enums.UserRoles;
import com.stripe.exception.StripeException;

public interface UserService {


    RegistrationtDTO registerUser(RegistrationtDTO registrationtDTO) throws StripeException;

    long countAllUserByRole(UserRoles role);

    long getNewFreelancerCountWeekly();

    long getNewClientCountWeekly();

    long getNewFreelancerCountMonthly();

    long getNewClientCountMonthly();
}
