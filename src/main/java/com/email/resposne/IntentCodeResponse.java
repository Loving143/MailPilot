package com.email.resposne;

import com.email.master.IntentCode;

public class IntentCodeResponse {
	
	private String body;
	private String code;
	private String description;
	private String subject;
	private String title;
	
	public IntentCodeResponse(IntentCode intentCode){
		this.body = intentCode.getBodyTemplate() ;
		this.code = intentCode.getCode();
		this.description = intentCode.getDescription();
		this.subject = intentCode.getSubjectTemplate();
		this.title = intentCode.getTitle();
		}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
