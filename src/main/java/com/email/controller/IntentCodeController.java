package com.email.controller;

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
	public String createIntentCode(@RequestBody IntentCodeRequest req){
		intentCodeService.registerIntentCode(req);
		return "Intentcode saved successfully.";
	}
	
	@GetMapping("/fetch/intentCode/{intentCode}")
	public IntentCodeResponse fetchIntentCode(@PathVariable String intentCode){
		return intentCodeService.fetchIntentCode(intentCode);
	}
	
	 @GetMapping("/fetch/intents/byCategoryCode/{category}")
	 public ResponseEntity<?> getIntentsByCategory(@PathVariable String category) {
	      return ResponseEntity.ok(intentCodeService.getIntentsByCategory(category));
	 }

}
