package com.email.entity;

import java.time.LocalDateTime;

import com.email.constants.EmailStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class RecentEmail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="recent_email_seq_id")
	@SequenceGenerator(name = "recent_email_seq_id",allocationSize = 1, initialValue = 1 )
	private Integer id;
	
	private String subject;
	
	private String body;
	
	private String recipientEmail;
	
	private LocalDateTime sentAt;
	
	@Enumerated(EnumType.STRING)
	private EmailStatus status;
	
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private Person person;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public EmailStatus getStatus() {
		return status;
	}

	public void setStatus(EmailStatus status) {
		this.status = status;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	
	
	

}
