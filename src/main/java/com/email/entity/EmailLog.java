package com.email.entity;

import java.time.LocalDateTime;

import com.email.constants.EmailStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "email_log")
public class EmailLog {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String name;
private String recipientEmail;
private String subject;
private LocalDateTime sentAt;
private String mobNo;
@Enumerated(EnumType.STRING)
private EmailStatus status;
private String company;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "parent_id")
private Person person;
private Integer reminderCount=0;
private String description;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRecipientEmail() {
    	return recipientEmail;
    }
    public void setRecipientEmail(String recipientEmail) {
    	this.recipientEmail = recipientEmail;
    }
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public LocalDateTime getSentAt() {
	return sentAt;
}
public void setSentAt(LocalDateTime sentAt) {
	this.sentAt = sentAt;
}
public String getMobNo() {
	return mobNo;
}
public void setMobNo(String mobNo) {
	this.mobNo = mobNo;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setReminderCount(Integer reminderCount) {
        this.reminderCount = reminderCount;
    }

    public Long getId() {
        return id;
    }

    public int getReminderCount() {
        return reminderCount != null ? reminderCount : 0;
    }

    public void setReminderCount(int reminderCount) {
        this.reminderCount = reminderCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }
}