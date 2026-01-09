package com.email.controller;

import java.io.File;
import java.io.IOException;

import com.email.exception.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume")
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
                return ResponseEntity.badRequest().body("File is empty");
            }
            
            File dir = new File(resumePath);
            if (!dir.exists()) {
                dir.mkdirs();
                logger.info("Created resume directory: {}", resumePath);
            }

            File resume = new File(dir, "Prateek_Kumar_Resume.pdf");
            file.transferTo(resume);
            
            logger.info("Resume uploaded successfully to: {}", resume.getAbsolutePath());
            return ResponseEntity.ok(new ApiResponse("1","Resume uploaded successfully"));
        } catch (Exception e) {
            logger.error("Error uploading resume", e);
            throw e;
        }
    }
}
