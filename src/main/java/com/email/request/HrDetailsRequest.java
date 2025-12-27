package com.email.request;

import com.email.constants.EmailStatus;

public class HrDetailsRequest {
	
	private String email;
	private String mobNo;
    private EmailStatus status;
    private String name;
    private String company;
    private String personEmail;
    private String body;
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
}
