package com.freelancemarketplace.backend.project.api.controller;

import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.project.application.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadFileController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/image")
    ApiResponse<?> uploadImahge(@RequestParam MultipartFile file) throws IOException {
        String imageUrl = cloudinaryService.uploadImageFile(file);
        return ApiResponse.success(imageUrl);
    }

    @PostMapping("/file-chat")
    ApiResponse<?> uploadFileChat(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File can not be empty");
        }
        String fileUrl = cloudinaryService.uploadFile(file);
        return ApiResponse.success(Map.of(
                "fileUrl", fileUrl,
                "fileName", file.getOriginalFilename()
        ));
    }
}
