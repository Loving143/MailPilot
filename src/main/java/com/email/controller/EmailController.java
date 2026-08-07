package com.email.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.email.exception.ApiResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.email.request.EmailIntentRequest;
import com.email.request.HrDetailsList;
import com.email.request.HrDetailsRequest;
import com.email.request.QuickSendRequest;
import com.email.service.EmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/email")
@CrossOrigin(originPatterns = "*", maxAge = 3600, allowCredentials = "true")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService service;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send")
    @Transactional
    public ResponseEntity<?> send(@RequestBody HrDetailsList hrDetails) {
        logger.info("Received request to send {} emails", hrDetails.getHrDetails().size());
        try {
            for (HrDetailsRequest req: hrDetails.getHrDetails()) {
                logger.debug("Sending email to: {}", req.getEmail());
                service.send(req);
                service.saveEmailLog(req);
            }
            logger.info("Successfully sent {} emails", hrDetails.getHrDetails().size());
            return ResponseEntity.ok(new ApiResponse("1","Emails sent successfully"));
        } catch (Exception e) {
            logger.error("Error sending emails", e);
            throw e;
        }
    }

    @GetMapping("/test-dns")
    public String testDns() throws Exception {
        InetAddress address = InetAddress.getByName("smtp.gmail.com");
        return address.getHostAddress();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("update/status")
    public  ResponseEntity<?> updateEmailStatus(@RequestBody HrDetailsList hrDetails) {
        logger.info("Received request to update status for {} emails", hrDetails.getHrDetails().size());
        try {
            for (HrDetailsRequest req: hrDetails.getHrDetails()) {
                logger.debug("Updating status for email: {} to status: {}", req.getEmail(), req.getStatus());
                service.updateEmailStatus(req.getEmail(),req.getStatus(),req.getMobNo(),req.getDescription());
            }
            logger.info("Successfully updated status for {} emails", hrDetails.getHrDetails().size());
            return ResponseEntity.ok(new ApiResponse("1","Emails updated successfully"));
        } catch (Exception e) {
            logger.error("Error updating email status", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("update/status/single")
    public ResponseEntity<?> updateSingleEmailStatus(@RequestBody HrDetailsRequest hrDetails) {
        logger.info("Received request to update status for single email: {}", hrDetails.getEmail());
        try {
            logger.debug("Updating status for email: {} to status: {}", hrDetails.getEmail(), hrDetails.getStatus());
            service.updateEmailStatus(hrDetails.getEmail(), hrDetails.getStatus(), hrDetails.getMobNo(),hrDetails.getDescription());
            logger.info("Successfully updated status for email: {}", hrDetails.getEmail());
            return ResponseEntity.ok(new ApiResponse("1","Email status updated successfully"));
        } catch (Exception e) {
            logger.error("Error updating email status for: {}", hrDetails.getEmail(), e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("generate/excel")
    public ResponseEntity<byte[]> generateExcel() throws IOException {
        logger.info("Generating Excel report for download");
        try {
            ByteArrayInputStream excel = service.generateExcel();
            byte[] excelBytes = excel.readAllBytes();
            
            logger.info("Excel report generated successfully, size: {} bytes", excelBytes.length);
            
            return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "attachment; filename=\"email_logs_" + 
                            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".xlsx\"")
                    .header("Content-Length", String.valueOf(excelBytes.length))
                    .body(excelBytes);
        } catch (Exception e) {
            logger.error("Error generating Excel report", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("generate/excel/email")
    public ResponseEntity<?> generateAndEmailExcel() throws IOException, MessagingException {
        logger.info("Generating and emailing Excel report");
        try {
            ByteArrayInputStream excel = service.generateExcel();
            service.sendExcel("Prateek.kumar949@gmail.com", excel.readAllBytes());
            logger.info("Excel report generated and sent via email successfully");
            return ResponseEntity.ok(new ApiResponse("1","Excel generated and sent via email successfully"));
        } catch (Exception e) {
            logger.error("Error generating and emailing Excel report", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("generate/excel/base64")
    public ResponseEntity<?> generateExcelAsBase64() throws IOException {
        logger.info("Generating Excel report as Base64");
        try {
            ByteArrayInputStream excel = service.generateExcel();
            byte[] excelBytes = excel.readAllBytes();
            String base64Excel = java.util.Base64.getEncoder().encodeToString(excelBytes);
            
            logger.info("Excel report generated as Base64, size: {} bytes", excelBytes.length);
            
            Map<String, Object> response = new HashMap<>();
            response.put("fileName", "email_logs_" + 
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".xlsx");
            response.put("fileContent", base64Excel);
            response.put("mimeType", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            
            return ResponseEntity.ok(new ApiResponse("1", "Excel generated successfully", response));
        } catch (Exception e) {
            logger.error("Error generating Excel report as Base64", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add/hrDetails")
    public ResponseEntity<?> addHrDetails(@RequestBody HrDetailsList hrDetails) {
        logger.info("Adding {} HR details", hrDetails.getHrDetails().size());
        try {
            for (HrDetailsRequest req : hrDetails.getHrDetails()) {
                logger.debug("Adding HR details for: {}", req.getEmail());
                service.addHrDetails(req);
            }
            logger.info("Successfully added {} HR details", hrDetails.getHrDetails().size());
            return ResponseEntity.ok(new ApiResponse("1","Information added successfully"));
        } catch (Exception e) {
            logger.error("Error adding HR details", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("quick-send")
    public ResponseEntity<?> quickSend(@RequestBody QuickSendRequest req){
        logger.info("Quick send request received for email: {}", req.getRecipientEmail());
        try {
            service.quickSend(req);
            logger.info("Quick send completed successfully for: {}", req.getRecipientEmail());
            return ResponseEntity.ok(new ApiResponse("1","Message sent successfully!!"));
        } catch (Exception e) {
            logger.error("Error in quick send for email: {}", req.getRecipientEmail(), e);
            throw e;
        }
    }



    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send/intent-email")
    public ResponseEntity<?>sendIntentEmail(@RequestBody EmailIntentRequest req){
        logger.info("Intent email request received");
        try {
            service.sendIntentEmail(req);
            logger.info("Intent email sent successfully");
            return ResponseEntity.ok(new ApiResponse("1","Message sent successfully!!"));
        } catch (Exception e) {
            logger.error("Error sending intent email", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("fetch/all")
    public ResponseEntity<?> fetchAllEmails(){
        logger.info("Fetching all emails");
        try {
            var result = service.fetchAllEmails();
            logger.info("Successfully fetched all emails");
            return ResponseEntity.ok(new ApiResponse("1", result));
        } catch (Exception e) {
            logger.error("Error fetching all emails", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fetch/{id}")
    public ResponseEntity<?> fetchEmailById(@PathVariable Long id) {
        logger.info("Fetching email by ID: {}", id);
        try {
            var result = service.fetchEmailById(id);
            logger.info("Successfully fetched email with ID: {}", id);
            return ResponseEntity.ok(new ApiResponse("1", result));
        } catch (Exception e) {
            logger.error("Error fetching email with ID: {}", id, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmailById(@PathVariable Long id){
        logger.info("Deleting email with ID: {}", id);
        try {
            service.deleteEmailById(id);
            logger.info("Successfully deleted email with ID: {}", id);
            return ResponseEntity.ok(new ApiResponse("1","Email log deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting email with ID: {}", id, e);
            throw e;
        }
    }
}