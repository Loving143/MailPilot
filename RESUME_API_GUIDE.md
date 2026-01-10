# Resume API Guide

## Overview

The Resume API provides endpoints for managing user resume files. Each authenticated user can upload, check status, and delete their resume file. The system supports multiple file formats and provides detailed file information.

## Features

- **User-specific file storage**: Each user's resume is stored with a unique filename
- **Multiple format support**: PDF, DOC, DOCX files
- **File status checking**: Get detailed information about uploaded resumes
- **Secure operations**: All endpoints require authentication
- **Automatic cleanup**: Delete operations remove user-specific files

## API Endpoints

### 1. Check Resume Status
**GET** `/resume/status`

**Description:** Check if the authenticated user has an uploaded resume file and get file details.

**Authentication:** Required (JWT token)

**Response Format:**

**When resume exists:**
```json
{
  "status": "1",
  "message": "Resume found",
  "data": {
    "hasResume": true,
    "fileName": "user_example_com_Resume.pdf",
    "fileSize": 245760,
    "lastModified": "2026-01-10 15:30:45",
    "filePath": "/path/to/resume/user_example_com_Resume.pdf"
  }
}
```

**When no resume exists:**
```json
{
  "status": "1",
  "message": "No resume uploaded",
  "data": {
    "hasResume": false,
    "fileName": null,
    "fileSize": null,
    "lastModified": null,
    "filePath": null
  }
}
```

### 2. Upload Resume
**POST** `/resume/upload`

**Description:** Upload a resume file for the authenticated user.

**Authentication:** Required (JWT token)

**Content-Type:** `multipart/form-data`

**Parameters:**
- `file` (required): The resume file to upload (PDF, DOC, DOCX)

**File Naming Convention:**
- Files are automatically renamed to: `{username}_Resume.{extension}`
- Example: `user_example_com_Resume.pdf`

**Response:**
```json
{
  "status": "1",
  "message": "Resume uploaded successfully"
}
```

**Error Response:**
```json
{
  "status": "0",
  "message": "File is empty"
}
```

### 3. Delete Resume
**DELETE** `/resume/delete`

**Description:** Delete the current user's resume file.

**Authentication:** Required (JWT token)

**Response (Success):**
```json
{
  "status": "1",
  "message": "Resume deleted successfully"
}
```

**Response (No file to delete):**
```json
{
  "status": "0",
  "message": "No resume found to delete"
}
```

## Frontend Integration Examples

### JavaScript/Fetch API

#### Check Resume Status
```javascript
async function checkResumeStatus() {
    try {
        const response = await fetch('/resume/status', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        
        const result = await response.json();
        
        if (result.status === '1') {
            const hasResume = result.data.hasResume;
            console.log('Has resume:', hasResume);
            
            if (hasResume) {
                console.log('File name:', result.data.fileName);
                console.log('File size:', result.data.fileSize, 'bytes');
                console.log('Last modified:', result.data.lastModified);
            }
        }
    } catch (error) {
        console.error('Error checking resume status:', error);
    }
}
```

#### Upload Resume
```javascript
async function uploadResume(file) {
    try {
        const formData = new FormData();
        formData.append('file', file);

        const response = await fetch('/resume/upload', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData
        });

        const result = await response.json();
        
        if (result.status === '1') {
            console.log('Resume uploaded successfully');
            // Refresh status
            await checkResumeStatus();
        } else {
            console.error('Upload failed:', result.message);
        }
    } catch (error) {
        console.error('Upload error:', error);
    }
}
```

#### Delete Resume
```javascript
async function deleteResume() {
    try {
        const response = await fetch('/resume/delete', {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        const result = await response.json();
        
        if (result.status === '1') {
            console.log('Resume deleted successfully');
            // Refresh status
            await checkResumeStatus();
        } else {
            console.error('Delete failed:', result.message);
        }
    } catch (error) {
        console.error('Delete error:', error);
    }
}
```

### React Hook Example
```jsx
import { useState, useEffect } from 'react';

const useResumeStatus = () => {
    const [resumeStatus, setResumeStatus] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const checkStatus = async () => {
        try {
            setLoading(true);
            const token = localStorage.getItem('authToken');
            
            const response = await fetch('/resume/status', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            const result = await response.json();
            
            if (result.status === '1') {
                setResumeStatus(result.data);
                setError(null);
            } else {
                setError(result.message);
            }
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const uploadResume = async (file) => {
        try {
            const token = localStorage.getItem('authToken');
            const formData = new FormData();
            formData.append('file', file);

            const response = await fetch('/resume/upload', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                body: formData
            });

            const result = await response.json();
            
            if (result.status === '1') {
                await checkStatus(); // Refresh status
                return { success: true, message: result.message };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: error.message };
        }
    };

    const deleteResume = async () => {
        try {
            const token = localStorage.getItem('authToken');
            
            const response = await fetch('/resume/delete', {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            const result = await response.json();
            
            if (result.status === '1') {
                await checkStatus(); // Refresh status
                return { success: true, message: result.message };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: error.message };
        }
    };

    useEffect(() => {
        checkStatus();
    }, []);

    return {
        resumeStatus,
        loading,
        error,
        checkStatus,
        uploadResume,
        deleteResume
    };
};

// Usage in component
const ResumeManager = () => {
    const { resumeStatus, loading, error, uploadResume, deleteResume } = useResumeStatus();

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            {resumeStatus?.hasResume ? (
                <div>
                    <p>Resume: {resumeStatus.fileName}</p>
                    <p>Size: {resumeStatus.fileSize} bytes</p>
                    <p>Modified: {resumeStatus.lastModified}</p>
                    <button onClick={deleteResume}>Delete Resume</button>
                </div>
            ) : (
                <div>
                    <p>No resume uploaded</p>
                    <input type="file" onChange={(e) => uploadResume(e.target.files[0])} />
                </div>
            )}
        </div>
    );
};
```

## File Storage Details

### File Naming Convention
- User-specific filenames prevent conflicts between users
- Format: `{username_sanitized}_Resume.{extension}`
- Example: `user@example.com` becomes `user_example_com_Resume.pdf`

### Supported File Types
- **PDF**: `.pdf`
- **Microsoft Word**: `.doc`, `.docx`

### File Location
- Files are stored in the directory specified by `resume.path` property
- Default location: `D:/email/` (configurable in application.properties)

### Backward Compatibility
- The system still supports the legacy filename format (`Prateek_Kumar_Resume.pdf`)
- New uploads use the user-specific naming convention

## Security Features

- **Authentication Required**: All endpoints require valid JWT token
- **User Isolation**: Each user can only access their own resume files
- **File Type Validation**: Only allowed file types can be uploaded
- **Secure File Operations**: All file operations are logged and validated

## Error Handling

### Common Error Responses

**Authentication Error (401):**
```json
{
  "status": "0",
  "message": "Access denied"
}
```

**File Not Found (when checking status):**
```json
{
  "status": "1",
  "message": "No resume uploaded",
  "data": {
    "hasResume": false
  }
}
```

**Upload Error:**
```json
{
  "status": "0",
  "message": "File is empty"
}
```

## Testing

Use the test page at `/resume-test.html` to verify API functionality:

1. Set your JWT token
2. Check current resume status
3. Upload a new resume file
4. Verify the status updates
5. Test delete functionality

## Configuration

### Application Properties
```properties
# Resume file storage path
resume.path=D:/email/

# File upload limits (if needed)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Directory Structure
```
resume.path/
├── user_example_com_Resume.pdf
├── admin_company_com_Resume.docx
└── Prateek_Kumar_Resume.pdf (legacy)
```