package com.email.resposne;


import com.email.entity.EmailLog;

import java.time.LocalDateTime;

public class EmailLogResponse {
    private String name;
    private String recipientEmail;
    private String subject;
    private LocalDateTime sentAt;
    private String mobNo;
    private String status;
    private String company;

    public EmailLogResponse() {}

    public EmailLogResponse(EmailLog emailLog) {
            if (emailLog == null) return;
            this.name = emailLog.getName();
            this.recipientEmail = emailLog.getRecipientEmail();
            this.subject = emailLog.getSubject();
            this.sentAt = emailLog.getSentAt();
            this.mobNo = emailLog.getMobNo();
            this.status = String.valueOf(emailLog.getStatus());
            this.company = emailLog.getCompany();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "EmailLogResponse{" +
                "name='" + name + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", sentAt=" + sentAt +
                ", mobNo='" + mobNo + '\'' +
                ", status='" + status + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}