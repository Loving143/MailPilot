package com.email.request;

import com.email.constants.EmailStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class HrDetailsRequest {
	
	@JsonProperty("email")
	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Email should be valid")
	private String email;
	
	@JsonProperty("mobNo")
	private String mobNo;
	
	@JsonProperty("status")
    private EmailStatus status;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("company")
    private String company;
    
    @JsonProperty("personEmail")
    private String personEmail;
    
    @JsonProperty("body")
    private String body;
    
    @JsonProperty("subject")
    private String subject;

    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }
	public String getBody() {
		return body;
	}
	
	public String getSubject() {
		return subject;
	}

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HrDetailsRequest{" +
                "email='" + email + '\'' +
                ", status=" + status +
                ", mobNo='" + mobNo + '\'' +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
