package com.freelancemarketplace.backend.service.imp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.freelancemarketplace.backend.service.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryServiceImp implements CloudinaryService {

    private final Cloudinary cloudinary;


    @Override
    public String uploadImageFile(MultipartFile file) throws IOException {

        assert file.getOriginalFilename() != null;
        String publicValue = genaratePublicValue(file.getOriginalFilename());
        String extension = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);

        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        String filePath = cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));
        cleanDisk(fileUpload);

        return filePath;
    }

    private File convert(MultipartFile file) throws IOException{
        assert file.getOriginalFilename() != null;
        File convertedFile = new File(StringUtils.join(genaratePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()){
            Files.copy(is, convertedFile.toPath());

        }
        return convertedFile;
    }


    private String genaratePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    private String[] getFileName(String originalName){
        return originalName.split("\\.");
    }

    private void cleanDisk(File file){
        try{
            Path filePath = file.toPath();
            Files.delete(filePath);
        }catch (IOException e){
            log.error("Clear disk errors: ", e);
        }
    }


    @Override
    public String uploadFile(MultipartFile file) throws IOException{
        Map<String, Object> uploadOptions = Map.of(
                "folder", "chat_attachments",
                "resource_type", "auto",
                "use_filename", "true",
                "unique_filenme", "false"
        );

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
        cleanDisk(convert(file));
        return uploadResult.get("secure_url").toString();
    }
}
