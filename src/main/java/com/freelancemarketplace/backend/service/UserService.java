package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.RegistrationtDTO;
import com.stripe.exception.StripeException;

public interface UserService {


    RegistrationtDTO registerUser(RegistrationtDTO registrationtDTO) throws StripeException;

}
