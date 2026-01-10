package com.email.controller;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.email.exception.ApiResponse;
import com.email.resposne.ResumeStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume")
@CrossOrigin(originPatterns = "*", maxAge = 3600, allowCredentials = "true")
public class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    @Value("${resume.path}")
    private String resumePath;

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file) throws IOException {

        logger.info("Resume upload request received, file size: {} bytes", file.getSize());
        
        try {
            if (file.isEmpty()) {
                logger.warn("Empty file uploaded");
                return ResponseEntity.badRequest().body(new ApiResponse("0", "File is empty"));
            }
            
            String currentUsername = getCurrentUsername();
            File dir = new File(resumePath);
            if (!dir.exists()) {
                dir.mkdirs();
                logger.info("Created resume directory: {}", resumePath);
            }

            // Use username-based filename for user-specific resumes
            String fileName = generateResumeFileName(currentUsername, file.getOriginalFilename());
            File resume = new File(dir, fileName);
            file.transferTo(resume);
            
            logger.info("Resume uploaded successfully for user: {} to: {}", currentUsername, resume.getAbsolutePath());
            return ResponseEntity.ok(new ApiResponse("1","Resume uploaded successfully"));
        } catch (Exception e) {
            logger.error("Error uploading resume", e);
            throw e;
        }
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getResumeStatus() {
        logger.info("Resume status request received");
        
        try {
            String currentUsername = getCurrentUsername();
            logger.debug("Checking resume status for user: {}", currentUsername);
            
            ResumeStatusResponse statusResponse = checkResumeStatus(currentUsername);
            
            String message = statusResponse.isHasResume() ? 
                "Resume found" : "No resume uploaded";
                
            logger.info("Resume status for user {}: {}", currentUsername, message);
            return ResponseEntity.ok(new ApiResponse("1", message, statusResponse));
            
        } catch (Exception e) {
            logger.error("Error checking resume status", e);
            throw e;
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteResume() {
        logger.info("Resume delete request received");
        
        try {
            String currentUsername = getCurrentUsername();
            logger.debug("Deleting resume for user: {}", currentUsername);
            
            boolean deleted = deleteUserResume(currentUsername);
            
            if (deleted) {
                logger.info("Resume deleted successfully for user: {}", currentUsername);
                return ResponseEntity.ok(new ApiResponse("1", "Resume deleted successfully"));
            } else {
                logger.warn("No resume found to delete for user: {}", currentUsername);
                return ResponseEntity.ok(new ApiResponse("0", "No resume found to delete"));
            }
            
        } catch (Exception e) {
            logger.error("Error deleting resume", e);
            throw e;
        }
    }

    // Helper methods
    private String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String generateResumeFileName(String username, String originalFileName) {
        // Extract file extension
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } else {
            extension = ".pdf"; // Default to PDF
        }
        
        // Create user-specific filename
        return username.replace("@", "_").replace(".", "_") + "_Resume" + extension;
    }

    private ResumeStatusResponse checkResumeStatus(String username) {
        File resumeDir = new File(resumePath);
        
        if (!resumeDir.exists()) {
            logger.debug("Resume directory does not exist: {}", resumePath);
            return new ResumeStatusResponse(false);
        }
        
        // Look for user-specific resume files
        String baseFileName = username.replace("@", "_").replace(".", "_") + "_Resume";
        File[] possibleFiles = resumeDir.listFiles((dir, name) -> 
            name.startsWith(baseFileName) && (name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx"))
        );
        
        // Also check for the legacy filename format
        File legacyFile = new File(resumeDir, "Prateek_Kumar_Resume.pdf");
        
        File resumeFile = null;
        
        if (possibleFiles != null && possibleFiles.length > 0) {
            // Use the most recent file if multiple exist
            resumeFile = possibleFiles[0];
            for (File file : possibleFiles) {
                if (file.lastModified() > resumeFile.lastModified()) {
                    resumeFile = file;
                }
            }
        } else if (legacyFile.exists()) {
            // Fallback to legacy file for backward compatibility
            resumeFile = legacyFile;
        }
        
        if (resumeFile != null && resumeFile.exists()) {
            long fileSize = resumeFile.length();
            LocalDateTime lastModified = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(resumeFile.lastModified()), 
                ZoneId.systemDefault()
            );
            String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            logger.debug("Resume found for user {}: {} ({} bytes)", username, resumeFile.getName(), fileSize);
            
            return new ResumeStatusResponse(
                true,
                resumeFile.getName(),
                fileSize,
                formattedDate,
                resumeFile.getAbsolutePath()
            );
        }
        
        logger.debug("No resume found for user: {}", username);
        return new ResumeStatusResponse(false);
    }

    private boolean deleteUserResume(String username) {
        File resumeDir = new File(resumePath);
        
        if (!resumeDir.exists()) {
            return false;
        }
        
        // Look for user-specific resume files
        String baseFileName = username.replace("@", "_").replace(".", "_") + "_Resume";
        File[] possibleFiles = resumeDir.listFiles((dir, name) -> 
            name.startsWith(baseFileName)
        );
        
        boolean deleted = false;
        if (possibleFiles != null) {
            for (File file : possibleFiles) {
                if (file.delete()) {
                    logger.info("Deleted resume file: {}", file.getName());
                    deleted = true;
                } else {
                    logger.warn("Failed to delete resume file: {}", file.getName());
                }
            }
        }
        
        return deleted;
    }
}
