# Email API Usage Guide

## JSON Format Issue Resolution

### Problem
You were getting this error:
```
MismatchedInputException: Cannot deserialize value of type `java.util.ArrayList<com.email.request.HrDetailsRequest>` from Object value
```

### Root Cause
The JSON structure being sent doesn't match what the API expects.

### Solutions

#### Option 1: Use the existing bulk endpoint with correct JSON format

**Endpoint:** `PUT /email/update/status`

**Correct JSON Format:**
```json
{
  "hrDetails": [
    {
      "email": "test@example.com",
      "status": "REPLIED",
      "mobNo": "1234567890"
    },
    {
      "email": "another@example.com", 
      "status": "NO_RESPONSE",
      "mobNo": "0987654321"
    }
  ]
}
```

#### Option 2: Use the new single update endpoint

**Endpoint:** `PUT /email/update/status/single`

**JSON Format:**
```json
{
  "email": "test@example.com",
  "status": "REPLIED", 
  "mobNo": "1234567890"
}
```

## Email Status Values

The `status` field accepts these values:
- `EMAIL_SENT`
- `REPLIED`
- `NO_RESPONSE`
- `INTERESTED`
- `NOT_INTERESTED`

## Complete API Endpoints

### Authentication Endpoints
**POST** `/api/auth/send-otp` - Send OTP for login
**POST** `/api/auth/verify-otp` - Verify OTP and get JWT token
**POST** `/api/auth/forgot-password` - Send password reset link
**POST** `/api/auth/reset-password` - Reset password with token

### Dashboard Statistics Endpoints

#### 1. Get Dashboard Stats
**GET** `/api/dashboard/stats`
- Returns main dashboard statistics (Total Emails, Sent Today, Response Rate, Active Campaigns)
- Includes growth percentages compared to previous periods

**Response:**
```json
{
  "status": "1",
  "message": "Dashboard stats retrieved successfully",
  "data": {
    "totalEmails": 150,
    "totalEmailsGrowth": "+12%",
    "sentToday": 5,
    "sentTodayGrowth": "+8%",
    "responseRate": 23.5,
    "responseRateGrowth": "+15%",
    "activeCampaigns": 3,
    "activeCampaignsGrowth": "+5%"
  }
}
```

#### 2. Get Detailed Stats
**GET** `/api/dashboard/stats/detailed`
- Returns comprehensive statistics with breakdowns

**Response:**
```json
{
  "status": "1",
  "message": "Detailed stats retrieved successfully",
  "data": {
    "totalEmails": 150,
    "sentToday": 5,
    "sentThisWeek": 25,
    "sentThisMonth": 80,
    "responseRate": 23.5,
    "totalResponses": 35,
    "statusBreakdown": {
      "Email Sent": 100,
      "Email Received": 30,
      "Interview_scheduled": 15,
      "Hired": 5
    },
    "activeCampaigns": 3,
    "uniqueCompanies": 45,
    "averageResponseTime": "2.5 days"
  }
}
```

#### 3. Get Stats by Date Range
**GET** `/api/dashboard/stats/range?startDate=2026-01-01&endDate=2026-01-31`
- Returns statistics for a specific date range

#### 4. Get Stats by Period
**GET** `/api/dashboard/stats/period/{period}`
- Available periods: `today`, `week`, `month`, `year`
- Returns detailed statistics for the specified period

#### 5. Get Dashboard Summary
**GET** `/api/dashboard/summary`
- Returns both dashboard and detailed stats in one response

#### 6. Get Quick Stats
**GET** `/api/dashboard/quick-stats`
- Returns simplified stats for quick display
- Optimized for mobile or widget displays

### User Management Endpoints

#### 1. User Logout

### 1. Send Emails
**POST** `/email/send`
```json
{
  "hrDetails": [
    {
      "email": "hr@company.com",
      "name": "John Doe",
      "company": "Tech Corp",
      "mobNo": "1234567890",
      "subject": "Job Application",
      "body": "Email content here"
    }
  ]
}
```

### 2. Update Email Status (Bulk)
**PUT** `/email/update/status`
```json
{
  "hrDetails": [
    {
      "email": "hr@company.com",
      "status": "REPLIED",
      "mobNo": "1234567890"
    }
  ]
}
```

### 3. Update Email Status (Single)
**PUT** `/email/update/status/single`
```json
{
  "email": "hr@company.com",
  "status": "REPLIED",
  "mobNo": "1234567890"
}
```

### 4. Quick Send
**POST** `/email/quick-send`
```json
{
  "recipientEmail": "hr@company.com"
}
```

### 5. Generate Excel Report (Download)
**GET** `/email/generate/excel`
- Returns Excel file as binary data for direct download
- Response headers include proper Content-Type and Content-Disposition
- Frontend should handle as blob/binary data

### 6. Generate Excel Report (Base64)
**GET** `/email/generate/excel/base64`
- Returns Excel file as Base64 encoded string
- Response format:
```json
{
  "status": "1",
  "message": "Excel generated successfully",
  "data": {
    "fileName": "email_logs_2026-01-10_15-30-45.xlsx",
    "fileContent": "UEsDBBQABgAIAAAAIQC2gziS...", 
    "mimeType": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
  }
}
```

### 7. Generate and Email Excel Report
**POST** `/email/generate/excel/email`
- Generates Excel file and sends it via email
- Returns success/failure message

### 8. Fetch All Emails
**GET** `/email/fetch/all`

### 9. Fetch Email by ID
**GET** `/email/fetch/{id}`

### 10. Delete Email by ID
**DELETE** `/email/delete/{id}`

## Frontend Excel Download Implementation

### Method 1: Binary Download (Recommended)
```javascript
async function downloadExcel() {
    const response = await fetch('/email/generate/excel', {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'email_logs.xlsx';
    a.click();
    window.URL.revokeObjectURL(url);
}
```

### Method 2: Base64 Download
```javascript
async function downloadExcelBase64() {
    const response = await fetch('/email/generate/excel/base64', {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    
    const result = await response.json();
    const { fileName, fileContent, mimeType } = result.data;
    
    // Convert base64 to blob
    const byteCharacters = atob(fileContent);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: mimeType });
    
    // Download
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    a.click();
    window.URL.revokeObjectURL(url);
}
```

## Error Handling

The API now provides detailed error messages for:
- Invalid JSON format
- Validation errors
- Missing required fields
- Authentication issues

## Logging

All operations are now logged with:
- Request details
- Processing steps
- Success/failure status
- Performance metrics
- Security events

Check the logs in the `logs/` directory:
- `emailoutreach.log` - General application logs
- `security.log` - Authentication and authorization logs  
- `email.log` - Email-specific operations

## Authentication

All endpoints (except OTP-related) require:
```
Authorization: Bearer <your-jwt-token>
```

## Validation

The API validates:
- Email format
- Required fields
- JSON structure
- Authentication tokens