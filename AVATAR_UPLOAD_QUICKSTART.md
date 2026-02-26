# Quick Start Guide - Avatar Upload

## Prerequisites

- Ensure your backend is running
- Ensure Cloudinary is properly configured (CLOUDINARY_URL environment variable set)

## Test the Feature

### Option 1: Using Postman

1. **Create a new POST request**
   - URL: `http://localhost:8080/api/freelancers/1/upload-avatar`
   - Method: POST

2. **Go to Body tab**
   - Select "form-data"
   - Add a new key-value pair:
     - Key: `file`
     - Type: `File` (dropdown)
     - Value: Select an image from your computer (JPG, PNG, GIF, etc.)

3. **Send the request**
   - Click "Send"
   - You should get a 200 OK response with the avatar URL

### Option 2: Using cURL (Terminal)

```bash
# Upload avatar for freelancer ID 1
curl -X POST http://localhost:8080/api/freelancers/1/upload-avatar \
  -F "file=@/path/to/your/image.jpg"
```

### Option 3: Using JavaScript (Frontend)

```javascript
// Function to upload avatar
async function uploadAvatar(freelancerId, imageFile) {
  const formData = new FormData();
  formData.append('file', imageFile);

  try {
    const response = await fetch(
      `/api/freelancers/${freelancerId}/upload-avatar`,
      {
        method: 'POST',
        body: formData
      }
    );
    
    const data = await response.json();
    
    if (data.statusCode === 'SUCCESS') {
      console.log('Avatar URL:', data.data.avatarUrl);
      return data.data.avatarUrl;
    } else {
      console.error('Upload failed:', data.message);
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
const fileInput = document.getElementById('avatarInput');
fileInput.addEventListener('change', (e) => {
  uploadAvatar(1, e.target.files[0]);
});
```

### Option 4: For Authenticated User

```bash
# Upload avatar for currently logged-in user
curl -X POST http://localhost:8080/api/freelancers/me/upload-avatar \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/your/image.jpg"
```

## Expected Success Response

```json
{
  "statusCode": "SUCCESS",
  "message": "Success",
  "data": {
    "avatarUrl": "https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/avatars/uuid_filename.jpg"
  }
}
```

## Expected Error Responses

### Empty File
```json
{
  "statusCode": "ERROR",
  "message": "File không được để trống",
  "data": null
}
```

### File Too Large (> 10MB)
```json
{
  "statusCode": "ERROR",
  "message": "File quá lớn, tối đa 10MB",
  "data": null
}
```

### Invalid File Type
```json
{
  "statusCode": "ERROR",
  "message": "File phải là hình ảnh (JPEG, PNG, GIF, WebP)",
  "data": null
}
```

### Freelancer Not Found
```json
{
  "statusCode": "ERROR",
  "message": "Freelancer with id: 9999 not found",
  "data": null
}
```

## Tips for Testing

1. **Use a small image** - For faster uploads during testing, use images under 2MB

2. **Verify in Cloudinary** - After uploading, check your Cloudinary dashboard:
   - Go to Media Library
   - Filter by "avatars" folder
   - You should see your uploaded images

3. **Check Database** - Query the Freelancers table to confirm the avatar URL is saved:
   ```sql
   SELECT freelancerId, avatar FROM Freelancers WHERE freelancerId = 1;
   ```

4. **Test Error Cases** - Try uploading:
   - An empty file
   - A non-image file (e.g., .txt)
   - A very large file (> 10MB)

## What Happens Behind the Scenes

1. **Validation**: File is validated for size and type
2. **Upload**: Image is sent to Cloudinary for processing
3. **Optimization**: Cloudinary automatically:
   - Resizes to 500x500 pixels
   - Detects faces and crops accordingly
   - Optimizes quality and format
4. **Storage**: Image is stored in Cloudinary's `avatars` folder
5. **Database Update**: The secure URL is saved in `FreelancerModel.avatar`
6. **Response**: The URL is returned to the client

## Troubleshooting

### Upload Fails with "Upload avatar lên Cloudinary thất bại"
- Check that `CLOUDINARY_URL` environment variable is set correctly
- Verify Cloudinary credentials are valid
- Check internet connectivity to Cloudinary API

### Upload Succeeds but Avatar URL Looks Wrong
- The URL should start with `https://res.cloudinary.com/`
- Check your Cloudinary account name in the URL
- Make sure the image is accessible from the URL

### Database Not Updating
- Check that the database connection is working
- Verify the freelancer ID exists in the database
- Check database permissions and transaction logs

## Next Steps

Once avatar upload is working:

1. **Retrieve Avatar**: Use the existing `/api/freelancers/{freelancerId}` endpoint to get the avatar URL
2. **Display Avatar**: Use the returned URL in your frontend to display the image
3. **Profile Picture**: Consider using the same upload mechanism for `profilePicture` field

---

**Need help?** Check the detailed documentation in `AVATAR_UPLOAD_IMPLEMENTATION.md`

