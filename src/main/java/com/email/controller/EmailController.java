package com.email.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.email.exception.ApiResponse;
import jakarta.transaction.Transactional;
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
public class EmailController {


@Autowired
private EmailService service;

@PreAuthorize("hasRole('USER')")
@PostMapping("/send")
@Transactional
public ResponseEntity<?> send(@RequestBody HrDetailsList hrDetails) {
	for (HrDetailsRequest req: hrDetails.getHrDetails()) {
	    service.send(req);
	    service.saveEmailLog(req);
	}
    return ResponseEntity.ok(new ApiResponse("1","Emails sent successfully"));
}

    @PreAuthorize("hasRole('USER')")
    @PutMapping("update/status")
    public  ResponseEntity<?> updateEmailStatus(@RequestBody HrDetailsList hrDetails) {
        for (HrDetailsRequest req: hrDetails.getHrDetails()) {
            service.updateEmailStatus(req.getEmail(),req.getStatus(),req.getMobNo());
        }
        return ResponseEntity.ok(new ApiResponse("1","Emails updated successfully"));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("generate/excel")
    public ResponseEntity<?> generateExcel() throws IOException, MessagingException {
        ByteArrayInputStream excel =  service.generateExcel();
        service.sendExcel("Prateek.kumar949@gmail.com", excel.readAllBytes());
        return ResponseEntity.ok(new ApiResponse("1","Excel generated successfully"));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add/hrDetails")
    public ResponseEntity<?> addHrDetails(@RequestBody HrDetailsList hrDetails) {
        for (HrDetailsRequest req : hrDetails.getHrDetails()) {
            service.addHrDetails(req);
        }
        return ResponseEntity.ok(new ApiResponse("1","Information added successfully"));

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("quick-send")
    public ResponseEntity<?> quickSend(@RequestBody QuickSendRequest req){
    	service.quickSend(req);
        return ResponseEntity.ok(new ApiResponse("1","Message sent successfully!!"));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send/intent-email")
    public ResponseEntity<?>sendIntentEmail(@RequestBody EmailIntentRequest req){
    	service.sendIntentEmail(req);
        return ResponseEntity.ok(new ApiResponse("1","Message sent successfully!!"));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("fetch/all")
    public ResponseEntity<?> fetchAllEmails(){
    	return ResponseEntity.ok(new ApiResponse("1",service.fetchAllEmails()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fetch/{id}")
    public ResponseEntity<?> fetchEmailById(@RequestBody Long id) {
        return ResponseEntity.ok(new ApiResponse("1",service.fetchEmailById(id)));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmailById(@PathVariable Long id){
        service.deleteEmailById(id);
        return ResponseEntity.ok(new ApiResponse("1","Email log deleted successfully"));
    }



    
    


}