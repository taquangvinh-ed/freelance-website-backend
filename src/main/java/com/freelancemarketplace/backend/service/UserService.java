package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.enums.UserRoles;
import com.stripe.exception.StripeException;

public interface UserService {


    RegistrationtDTO registerUser(RegistrationtDTO registrationtDTO) throws StripeException;

    long countAllUserByRole(UserRoles role);

    long getNewFreelancerCountWeekly();

    long getNewClientCountWeekly();

    long getNewFreelancerCountMonthly();

    long getNewClientCountMonthly();
}
