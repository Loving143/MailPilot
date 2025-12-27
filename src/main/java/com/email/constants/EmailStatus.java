package com.email.constants;

public enum EmailStatus {
    EMAIL_SENT("Email Sent"),
    EMAIL_RECEIVED("Email Received"),
    CONTACTED_ON_PHONE("Contacted_On_Phone"),
    Interview_Scheduled("Interview_scheduled"),
    REJECTED("Rejected"),
    HIRED("Hired"),
    ;

    private final String value;
    EmailStatus(String displayName) {
        this.value = displayName;
    }

    public String getValue() {
        return value;
    }
}
