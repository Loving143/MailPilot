package com.email.enumm;

public enum OtpType {

    EMAIL("Email"),
    MOBILE("Mobile"),
    USER_ID("User Id"),
    DEVICE_ID("Device Id");

    private String value;

    OtpType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
