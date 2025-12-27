package com.email.enumm;

public enum OtpChannel {

    EMAIL("Email"),
    SMS("Sms"),
    WHATSAPP("Whatsapp"),
    PUSH("Push");

    private String value;
    OtpChannel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
