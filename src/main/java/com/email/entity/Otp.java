package com.email.entity;

import com.email.enumm.OtpType;
import com.email.enumm.OtpChannel;
import com.email.enumm.OtpPurpose;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "otp_sequence_id")
    @SequenceGenerator(sequenceName ="otp_sequence_id",allocationSize = 1,initialValue =1, name = "otp_sequence_id")
    private Integer id;

    @Column(nullable = false)
    private String otp;

    @ManyToOne(fetch= FetchType.LAZY,cascade = CascadeType.ALL)
    private Person person;

    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose;

    private LocalDateTime expiryAt;

    private int maxAttempts = 5;

    private int currentAttempts;

    private boolean used;

    private LocalDateTime createdAt;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public OtpType getIdentifierType() {
        return otpType;
    }

    public void setIdentifierType(OtpType otpType) {
        this.otpType = otpType;
    }

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OtpPurpose purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getCurrentAttempts() {
        return currentAttempts;
    }

    public void setCurrentAttempts(int currentAttempts) {
        this.currentAttempts = currentAttempts;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
