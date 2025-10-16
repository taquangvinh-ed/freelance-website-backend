package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadFileController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/image")
    ResponseEntity<ResponseDTO> uploadImahge(@RequestParam MultipartFile file) throws IOException {
        String imageUrl = cloudinaryService.uploadImageFile(file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        imageUrl
                ));
    }

    @PostMapping("/file-chat")
    ResponseEntity<?> uploadFileChat(@RequestParam MultipartFile file){
        if(file.isEmpty())
            return new ResponseEntity<>("File can not be empty", HttpStatus.BAD_REQUEST);
        try{
            String fileUrl = cloudinaryService.uploadFile(file);
            return new ResponseEntity<>(Map.of(
                    "fileUrl", fileUrl,
                    "fileName", file.getOriginalFilename()
            ),
                    HttpStatus.OK);
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);        }
    }
}
