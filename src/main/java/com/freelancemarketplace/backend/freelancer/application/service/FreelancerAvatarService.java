package com.freelancemarketplace.backend.freelancer.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FreelancerAvatarService {

    String uploadAvatar(Long freelancerId, MultipartFile file) throws IOException;
}
