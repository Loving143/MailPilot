package com.email.enumm;

public enum OtpPurpose {

    REGISTRATION("Registration"),
    LOGIN("Login"),
    PASSWORD_RESET("Password Reset"),
    EMAIL_VERIFICATION("Email Verification"),
    MOBILE_VERIFICATION("Mobile Verification"),
    CHANGE_PASSWORD("Change Password"),
    CHANGE_EMAIL("Change Email"),
    CHANGE_MOBILE("Change Mobile"),
    TRANSACTION_CONFIRMATION("Transaction Confirmation"),
    MFA_SECOND_FACTOR("Mfa Second Factor"),
    DEVICE_VERIFICATION("Device Verification"),
    TEST_OTP("Test otp");

    private String value;

    OtpPurpose(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
