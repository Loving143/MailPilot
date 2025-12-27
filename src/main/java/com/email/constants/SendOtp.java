package com.email.constants;

public class SendOtp {

    public static final String SUBJECT =
            "Your mailPilot OTP for Registration";

    public static String body(String otp) {
        return """
            Dear User,

            Thank you for registering with mailPilot.

            To complete your registration, please use the following One-Time Password (OTP):

            %s

            This OTP is valid for 10 minutes.
            For security reasons, please do not share this OTP with anyone.

            If you did not request this registration, please ignore this email.

            Warm regards,
            Team mailPilot
            """.formatted(otp);
    }
}
