package com.email.controller;

import com.email.exception.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.email.request.IntentCodeRequest;
import com.email.resposne.IntentCodeResponse;
import com.email.service.IntentCodeService;

@RestController
public class IntentCodeController {
	
	@Autowired
	private IntentCodeService intentCodeService;
	
	// create a method to return current date

	
	@PostMapping("/create/intent-code")
	public ResponseEntity<?> createIntentCode(@RequestBody IntentCodeRequest req){
		intentCodeService.registerIntentCode(req);
		return ResponseEntity.ok(new ApiResponse("1","Intentcode saved successfully."));
	}
	
	@GetMapping("/fetch/intentCode/{intentCode}")
	public ResponseEntity<?> fetchIntentCode(@PathVariable String intentCode){
		return ResponseEntity.ok(new ApiResponse("1", intentCodeService.fetchIntentCode(intentCode)));
	}
	
	 @GetMapping("/fetch/intents/byCategoryCode/{category}")
	 public ResponseEntity<?> getIntentsByCategory(@PathVariable String category) {
	      return ResponseEntity.ok(new ApiResponse("1",intentCodeService.getIntentsByCategory(category)));
	 }

}
