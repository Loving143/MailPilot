<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MailPilot OTP Verification</title>
</head>

<body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, sans-serif;">

<table width="100%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:20px;">
    <tr>
        <td align="center">

            <table width="600" cellpadding="0" cellspacing="0"
                   style="background:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 4px 10px rgba(0,0,0,0.08);">

                <!-- Header -->
                <tr>
                    <td style="background:#2563eb; padding:20px; text-align:center;">
                        <h1 style="color:#ffffff; margin:0; font-size:26px;">
                            ✈ MailPilot
                        </h1>
                        <p style="color:#dbeafe; margin:5px 0 0;">
                            Secure Authentication
                        </p>
                    </td>
                </tr>

                <!-- Content -->
                <tr>
                    <td style="padding:30px; color:#1f2937;">
                        <p style="font-size:16px;">Dear <strong>${userName}</strong>,</p>

                        <p style="font-size:15px;">
                            Thank you for registering with <strong>MailPilot</strong>.
                            Please use the following One-Time Password (OTP) to complete your verification:
                        </p>

                        <!-- OTP Box -->
                        <div style="
                            margin:25px 0;
                            padding:18px;
                            text-align:center;
                            background:#f1f5f9;
                            border-radius:6px;
                            font-size:28px;
                            letter-spacing:6px;
                            font-weight:bold;
                            color:#2563eb;">
                            ${otp}
                        </div>

                        <p style="font-size:14px; color:#374151;">
                            ⏱ This OTP is valid for <strong>${expiryMinutes} minutes</strong>.
                        </p>

                        <p style="font-size:14px; color:#374151;">
                            For security reasons, please do not share this OTP with anyone.
                        </p>

                        <p style="font-size:14px;">
                            If you did not request this, please ignore this email.
                        </p>

                        <br>

                        <p style="font-size:15px;">
                            Warm regards,<br>
                            <strong>Team MailPilot</strong>
                        </p>
                    </td>
                </tr>

                <!-- Footer -->
                <tr>
                    <td style="background:#f9fafb; padding:15px; text-align:center; font-size:12px; color:#6b7280;">
                        © 2025 MailPilot. All rights reserved.<br>
                        This is an automated message. Please do not reply.
                    </td>
                </tr>

            </table>

        </td>
    </tr>
</table>

</body>
</html>
