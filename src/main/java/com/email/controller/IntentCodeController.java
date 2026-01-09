package com.email.controller;

import com.email.exception.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.email.request.IntentCodeRequest;
import com.email.resposne.IntentCodeResponse;
import com.email.service.IntentCodeService;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600, allowCredentials = "true")
public class IntentCodeController {

    private static final Logger logger = LoggerFactory.getLogger(IntentCodeController.class);
	
	@Autowired
	private IntentCodeService intentCodeService;
	
	@PostMapping("/create/intent-code")
	public ResponseEntity<?> createIntentCode(@RequestBody IntentCodeRequest req){
        logger.info("Creating new intent code: {}", req.getCode());
        try {
            intentCodeService.registerIntentCode(req);
            logger.info("Intent code created successfully: {}", req.getCode());
            return ResponseEntity.ok(new ApiResponse("1","Intentcode saved successfully."));
        } catch (Exception e) {
            logger.error("Error creating intent code: {}", req.getCode(), e);
            throw e;
        }
	}
	
	@GetMapping("/fetch/intentCode/{intentCode}")
	public ResponseEntity<?> fetchIntentCode(@PathVariable String intentCode){
        logger.info("Fetching intent code: {}", intentCode);
        try {
            var result = intentCodeService.fetchIntentCode(intentCode);
            logger.info("Intent code fetched successfully: {}", intentCode);
            return ResponseEntity.ok(new ApiResponse("1", result));
        } catch (Exception e) {
            logger.error("Error fetching intent code: {}", intentCode, e);
            throw e;
        }
	}
	
	 @GetMapping("/fetch/intents/byCategoryCode/{category}")
	 public ResponseEntity<?> getIntentsByCategory(@PathVariable String category) {
         logger.info("Fetching intents by category: {}", category);
         try {
             var intents = intentCodeService.getIntentsByCategory(category);
             logger.info("Successfully fetched {} intents for category: {}", intents.size(), category);
             return ResponseEntity.ok(new ApiResponse("1", intents));
         } catch (Exception e) {
             logger.error("Error fetching intents by category: {}", category, e);
             throw e;
         }
	 }
}
