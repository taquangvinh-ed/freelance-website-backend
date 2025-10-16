package com.freelancemarketplace.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadImageFile(MultipartFile file) throws IOException;

    String uploadFile(MultipartFile file) throws IOException;
}
