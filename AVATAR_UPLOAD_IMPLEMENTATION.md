# Avatar Upload Implementation Guide

## Overview
This document describes the avatar upload functionality implemented for the Freelancer Marketplace Backend. The implementation allows freelancers to upload their avatar images to Cloudinary and store the URL in the `FreelancerModel.avatar` field.

## Components Modified/Created

### 1. **CloudinaryService Interface**
**File**: `src/main/java/com/freelancemarketplace/backend/service/CloudinaryService.java`

**New Method Added**:
```java
String uploadAvatar(MultipartFile file) throws IOException;
```

### 2. **CloudinaryServiceImp Implementation**
**File**: `src/main/java/com/freelancemarketplace/backend/service/imp/CloudinaryServiceImp.java`

**New Method Implementation**:
```java
@Override
public String uploadAvatar(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
        throw new IllegalArgumentException("File không được để trống");
    }

    // Validate file size (max 10MB for avatars)
    if (file.getSize() > 10 * 1024 * 1024) {
        throw new IllegalArgumentException("File quá lớn, tối đa 10MB");
    }

    // Validate file type
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
        throw new IllegalArgumentException("File phải là hình ảnh (JPEG, PNG, GIF, WebP)");
    }

    try {
        Map<String, Object> uploadOptions = Map.of(
                "folder", "avatars",
                "resource_type", "image",
                "quality", "auto",
                "fetch_format", "auto",
                "width", 500,
                "height", 500,
                "crop", "fill",
                "gravity", "face",
                "use_filename", true,
                "unique_filename", true
        );

        Map uploadResult = cloudinary.uploader().upload(file.getInputStream(), uploadOptions);
        return uploadResult.get("secure_url").toString();

    } catch (Exception e) {
        throw new IOException("Upload avatar lên Cloudinary thất bại: " + e.getMessage(), e);
    }
}
```

**Key Features**:
- Stores avatars in the **`avatars` folder** in Cloudinary
- Automatically resizes images to 500x500 pixels using face detection
- Validates file size (max 10MB)
- Validates file type (must be image)
- Returns the secure_url from Cloudinary

### 3. **FreelancerService Interface**
**File**: `src/main/java/com/freelancemarketplace/backend/service/FreelancerService.java`

**New Method Added**:
```java
String uploadAvatar(Long freelancerId, MultipartFile file) throws IOException;
```

**New Import**:
```java
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
```

### 4. **FreelancerServiceImp Implementation**
**File**: `src/main/java/com/freelancemarketplace/backend/service/imp/FreelancerServiceImp.java`

**New Dependency Injected**:
```java
private final CloudinaryService cloudinaryService;
```

**New Method Implementation**:
```java
@Override
@Transactional
public String uploadAvatar(Long freelancerId, MultipartFile file) throws IOException {
    FreelancerModel freelancer = freelancersRepository.findById(freelancerId)
            .orElseThrow(() -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found"));

    // Upload avatar to Cloudinary
    String avatarUrl = cloudinaryService.uploadAvatar(file);

    // Update freelancer's avatar field
    freelancer.setAvatar(avatarUrl);
    freelancersRepository.save(freelancer);

    log.info("Avatar uploaded successfully for freelancer: {}", freelancerId);
    return avatarUrl;
}
```

### 5. **FreelancerController**
**File**: `src/main/java/com/freelancemarketplace/backend/controller/FreelancerController.java`

**New Imports**:
```java
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
```

**New Endpoints**:

#### Upload Avatar by ID
```java
@PostMapping("/{freelancerId}/upload-avatar")
public ResponseEntity<ResponseDTO> uploadAvatar(
        @PathVariable Long freelancerId,
        @RequestParam MultipartFile file) throws IOException {
    String avatarUrl = freelancerService.uploadAvatar(freelancerId, file);
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDTO(
                    ResponseStatusCode.SUCCESS,
                    ResponseMessage.SUCCESS,
                    Map.of("avatarUrl", avatarUrl)
            ));
}
```

#### Upload Own Avatar (Authenticated)
```java
@PostMapping("/me/upload-avatar")
public ResponseEntity<ResponseDTO> uploadMyAvatar(
        @AuthenticationPrincipal AppUser appUser,
        @RequestParam MultipartFile file) throws IOException {
    Long freelancerId = appUser.getId();
    String avatarUrl = freelancerService.uploadAvatar(freelancerId, file);
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseDTO(
                    ResponseStatusCode.SUCCESS,
                    ResponseMessage.SUCCESS,
                    Map.of("avatarUrl", avatarUrl)
            ));
}
```

## Cloudinary Configuration

**Important**: NO NEED to create a folder manually in Cloudinary. The implementation automatically stores all avatars in the `avatars` folder through the upload configuration.

The existing `CloudinaryConfig.java` reads from the `CLOUDINARY_URL` environment variable which is already configured.

## API Usage

### Endpoint 1: Upload Avatar for Specific Freelancer
```
POST /api/freelancers/{freelancerId}/upload-avatar
Content-Type: multipart/form-data

Parameters:
- freelancerId (path): The ID of the freelancer
- file (form-data): The image file to upload
```

**Response**:
```json
{
    "statusCode": "SUCCESS",
    "message": "Success",
    "data": {
        "avatarUrl": "https://res.cloudinary.com/your-cloud/image/upload/v1234567890/avatars/filename.jpg"
    }
}
```

### Endpoint 2: Upload Own Avatar (Requires Authentication)
```
POST /api/freelancers/me/upload-avatar
Content-Type: multipart/form-data

Parameters:
- file (form-data): The image file to upload

Headers:
- Authorization: Bearer <token>
```

**Response**:
```json
{
    "statusCode": "SUCCESS",
    "message": "Success",
    "data": {
        "avatarUrl": "https://res.cloudinary.com/your-cloud/image/upload/v1234567890/avatars/filename.jpg"
    }
}
```

## Error Handling

The implementation includes validation for:
- **Empty file**: Returns "File không được để trống"
- **File too large (>10MB)**: Returns "File quá lớn, tối đa 10MB"
- **Invalid file type**: Returns "File phải là hình ảnh (JPEG, PNG, GIF, WebP)"
- **Freelancer not found**: Returns "Freelancer with id: {id} not found"
- **Cloudinary upload failure**: Returns error message from Cloudinary

## Cloudinary Optimization Features

The upload configuration includes:
- **Auto Quality**: Automatically adjusts image quality for optimal file size
- **Auto Format**: Converts image to optimal format (WebP, etc.)
- **Automatic Resizing**: 500x500 pixels
- **Face Detection Crop**: Uses face detection for better cropping
- **Unique Filename**: Prevents filename collisions

## Database Changes

No database schema changes required. The `FreelancerModel` already has the `avatar` field:
```java
private String avatar;
```

## Testing

You can test the implementation using:

### cURL:
```bash
curl -X POST http://localhost:8080/api/freelancers/1/upload-avatar \
  -F "file=@/path/to/image.jpg"
```

### With Authentication:
```bash
curl -X POST http://localhost:8080/api/freelancers/me/upload-avatar \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@/path/to/image.jpg"
```

### Postman:
1. Create a POST request to `http://localhost:8080/api/freelancers/{freelancerId}/upload-avatar`
2. Go to "Body" tab
3. Select "form-data"
4. Add key "file" with type "File"
5. Upload your image
6. Send request

## Summary

✅ Avatar upload functionality fully implemented
✅ Cloudinary integration configured
✅ Validation for file size and type
✅ Automatic image optimization and resizing
✅ Stored in "avatars" folder in Cloudinary
✅ Two endpoints provided (authenticated and by ID)
✅ Error handling implemented

