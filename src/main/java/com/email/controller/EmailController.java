package com.email.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


@PostMapping("/send")
public String send(@RequestBody HrDetailsList hrDetails) {
	for (HrDetailsRequest req: hrDetails.getHrDetails()) {
	    service.send(req);
	    service.saveEmailLog(req);
	}
	return "Emails sent successfully";
}

    @PutMapping("update/status")
    public String updateEmailStatus(@RequestBody HrDetailsList hrDetails) {
        for (HrDetailsRequest req: hrDetails.getHrDetails()) {
            service.updateEmailStatus(req.getEmail(),req.getStatus(),req.getMobNo());
        }
        return "Emails updated successfully";
    }

    @GetMapping("generate/excel")
    public String generateExcel() throws IOException, MessagingException {
        ByteArrayInputStream excel =  service.generateExcel();
        service.sendExcel("Prateek.kumar949@gmail.com", excel.readAllBytes());
    return "Excel generated successfully";
    }

    @PostMapping("/add/hrDetails")
    public String addHrDetails(@RequestBody HrDetailsList hrDetails) {
        for (HrDetailsRequest req : hrDetails.getHrDetails()) {
            service.addHrDetails(req);
        }
        return "Information added successfully";
    }
    
    @PostMapping("quick-send")
    public ResponseEntity<?> quickSend(@RequestBody QuickSendRequest req){
    	service.quickSend(req);
    	return ResponseEntity.ok("Message sent successfully!!");
    }
    
    @PostMapping("/send/intent-email")
    public ResponseEntity<?>sendIntentEmail(@RequestBody EmailIntentRequest req){
    	service.sendIntentEmail(req);
    	return ResponseEntity.ok("Message sent successfully!!");
    }

    @GetMapping("fetch/all")
    public ResponseEntity<?> fetchAllEmails(){
    	return ResponseEntity.ok(service.fetchAllEmails());
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<?> fetchEmailById(@RequestBody Long id) {
        return ResponseEntity.ok(service.fetchEmailById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmailById(@PathVariable Long id){
        service.deleteEmailById(id);
        return ResponseEntity.ok("Email log deleted successfully");
    }



    
    


}