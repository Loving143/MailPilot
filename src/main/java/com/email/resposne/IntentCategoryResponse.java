package com.email.resposne;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.email.master.IntentCategory;
import com.email.master.IntentCode;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

public class IntentCategoryResponse {
	
	private String categoryCode;
	
	private String name;
	
	private String description;
	
	private List<IntentCodeResponse>intentCodes ;
	
	public IntentCategoryResponse(IntentCategory cat){
		this.categoryCode = cat.getCategoryCode();
		this.name = cat.getName();
		this.description = cat.getDescription();
		this.intentCodes = cat.getIntentCodes().stream().map(IntentCodeResponse::new).collect(Collectors.toList());
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<IntentCodeResponse> getIntentCodes() {
		return intentCodes;
	}
	public void setIntentCodes(List<IntentCodeResponse> intentCodes) {
		this.intentCodes = intentCodes;
	}
	
	


}
