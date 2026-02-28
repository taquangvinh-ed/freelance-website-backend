# Account Verification Feature - Quick Start Guide

## What I've Implemented

I've created a complete account verification system with:

### ✅ Files Created

1. **Enum**: `VerificationStatus.java` - Status tracking (PENDING, OTP_VERIFIED, APPROVED, REJECTED)
2. **Model**: `AccountVerificationModel.java` - Database entity
3. **Repository**: `AccountVerificationRepository.java` - Data access layer
4. **DTO**: `AccountVerificationDTO.java` - Data transfer object
5. **Requests**: 
   - `VerifyOtpRequest.java` - OTP verification request
   - `ReviewVerificationRequest.java` - Admin review request
6. **Service**: `AccountVerificationService.java` and `AccountVerificationServiceImp.java`
7. **Controller**: `AccountVerificationController.java`

### ✅ Updated Files

1. **EmailServiceImp.java** - Fixed sendHtmlEmail method to properly send HTML emails

## Setup Instructions

### Step 1: Create Database Table

Run this SQL in your PostgreSQL database:

```sql
CREATE TABLE account_verifications (
    verification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(user_id),
    citizen_id_card_url VARCHAR(500) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    otp_generated_at TIMESTAMP NOT NULL,
    otp_expires_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    reviewed_by_admin_id BIGINT REFERENCES admin(admin_id),
    rejection_reason TEXT,
    reviewed_at TIMESTAMP,
    submitted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_verification_user_id ON account_verifications(user_id);
CREATE INDEX idx_verification_status ON account_verifications(status);
```

### Step 2: Cloudinary Configuration

**Option A: Use Existing Configuration** (No action needed)
- The system uses `CloudinaryService.uploadFile()` which uploads to `chat_attachments` folder

**Option B: Create Dedicated Folder** (Recommended)
If you want a separate folder for ID cards, you can:
1. Create a folder named `id_cards` in your Cloudinary account (optional - Cloudinary creates it automatically)
2. Or keep using the existing implementation

### Step 3: Email Configuration

Ensure your `application.yml` has email configuration:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: taquangvinh.study@gmail.com
    password: <your-app-password>
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### Step 4: Build and Run

```bash
cd /home/tqvinh/Workspaces/freelancer-marketplace/freelance-website-backend
./gradlew clean build -x test
./gradlew bootRun
```

## API Endpoints Summary

### 📱 User Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/verification/submit-id-card` | Upload Citizen ID Card | User |
| POST | `/api/verification/verify-otp` | Verify OTP code | User |
| POST | `/api/verification/resend-otp` | Resend OTP | User |
| GET | `/api/verification/status` | Check verification status | User |

### 👨‍💼 Admin Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/verification/admin/pending` | Get pending verifications | Admin |
| GET | `/api/verification/admin/by-status/{status}` | Get verifications by status | Admin |
| PUT | `/api/verification/admin/review/{verificationId}` | Approve/reject verification | Admin |

## Frontend Integration

### Step 1: Upload ID Card

```javascript
const uploadIdCard = async (idCardFile) => {
    const formData = new FormData();
    formData.append('idCard', idCardFile);
    
    const response = await fetch('http://localhost:8080/api/verification/submit-id-card', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${yourAuthToken}`
        },
        body: formData
    });
    
    const result = await response.json();
    if (response.ok) {
        // Show success message: "OTP sent to your email"
        // Navigate to OTP input screen
    }
};
```

### Step 2: Verify OTP

```javascript
const verifyOtp = async (otpCode) => {
    const response = await fetch('http://localhost:8080/api/verification/verify-otp', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${yourAuthToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ otpCode })
    });
    
    const result = await response.json();
    if (response.ok) {
        // Show success message: "OTP verified! Waiting for admin approval"
        // Navigate to waiting/status screen
    }
};
```

### Admin: Review Verification

```javascript
const reviewVerification = async (verificationId, approved, rejectionReason = null) => {
    const response = await fetch(`http://localhost:8080/api/verification/admin/review/${verificationId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${adminAuthToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ 
            approved, 
            rejectionReason 
        })
    });
    
    return await response.json();
};
```

## Workflow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    USER REGISTRATION                         │
│                  (Account Status: PENDING)                   │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 1: Upload Citizen ID Card                              │
│  POST /api/verification/submit-id-card                       │
│  - Upload photo to Cloudinary                                │
│  - Generate 6-digit OTP (expires in 10 min)                  │
│  - Send OTP email                                            │
│  - Status: PENDING                                           │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 2: User Enters OTP                                     │
│  POST /api/verification/verify-otp                           │
│  - Validate OTP code                                         │
│  - Check expiration                                          │
│  - Status: OTP_VERIFIED                                      │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│  STEP 3: Admin Review                                        │
│  GET /api/verification/admin/pending                         │
│  PUT /api/verification/admin/review/{id}                     │
│                                                              │
│  ┌──────────────────┐              ┌──────────────────┐     │
│  │   APPROVED       │              │    REJECTED      │     │
│  │  Status: APPROVED│              │  Status: REJECTED│     │
│  │  Account: ACTIVE │              │  Can Resubmit    │     │
│  │  Email sent      │              │  Email sent      │     │
│  └──────────────────┘              └──────────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

## OTP Email Example

When a user submits their ID card, they receive an email like:

```
Subject: Verify Your Account - OTP Code

Dear John Doe,

Your OTP code for account verification is:

123456

This code will expire in 10 minutes.

If you didn't request this verification, please ignore this email.

Best regards,
Freelancer Marketplace Team
```

## Security Features

✅ OTP expires in 10 minutes
✅ Only one active verification per user
✅ Cannot resubmit if already approved
✅ Admin-only access to review endpoints
✅ File uploaded to secure Cloudinary storage
✅ Email validation

## Testing Checklist

- [ ] Create database table
- [ ] Configure email settings in application.yml
- [ ] Test user flow:
  - [ ] Upload ID card → OTP email received
  - [ ] Verify OTP → Status changes to OTP_VERIFIED
  - [ ] Resend OTP → New OTP received
  - [ ] Check status → Correct status returned
- [ ] Test admin flow:
  - [ ] View pending verifications
  - [ ] Approve verification → User account becomes ACTIVE
  - [ ] Reject verification → User receives rejection email
- [ ] Test edge cases:
  - [ ] Expired OTP
  - [ ] Invalid OTP
  - [ ] Already verified account
  - [ ] Missing rejection reason

## Notes

1. **Cloudinary Folder**: The current implementation uploads ID cards to the default folder used by `uploadFile()`. If you want a dedicated folder, let me know and I'll update the code.

2. **OTP Storage**: Currently OTP is stored as plain text. For production, consider hashing it.

3. **Rate Limiting**: Consider adding rate limiting to prevent OTP spam.

4. **Notification Integration**: You can integrate with your existing notification system to notify admins in real-time when new verifications arrive.

## Need Help?

If you encounter any issues:
1. Check the database table was created correctly
2. Verify email configuration in application.yml
3. Check Cloudinary credentials are configured
4. Review application logs for detailed error messages

