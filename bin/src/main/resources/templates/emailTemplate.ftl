<!DOCTYPE html>
<html>
<head>
    <style>
        .email-container {
            font-family: Arial, sans-serif;
            color: #333;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            max-width: 600px;
            margin: auto;
        }
        .logo {
        width: 100px;
        height: auto;
    	}
         .header {
            background-color: #FFFFFF;
            color: #000000;
            padding: 10px;
            text-align: center;
        }
        .content {
            margin: 20px 0;
        }
        .otp {
            font-size: 24px;
            font-weight: bold;
            color: #0044cc;
            text-align: center;
            margin: 20px 0;
        }
        .footer {
            margin-top: 30px;
            font-size: 12px;
            color: #777;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="email-container">
    	 
        <div class="header">
        	 <img src="cid:logoImage" alt="Your MailPilot Logo" class="logo">
            <h1>MailPilot Secure Login</h1>
        </div>
        
        <div class="content">
            <p>Dear ${name},</p>
            <p>You are trying to log in to your MailPilot account. Use the OTP below to complete the process:</p>
            <div class="otp">${otp}</div>
            <p>This OTP is valid for 5 minutes. Please do not share it with anyone.</p>
            <p>If you did not request this login, please contact our support team immediately.</p>
        </div>
        <div class="footer">
            <p>&copy; 2025 MailPilot. All Rights Reserved.</p>
        </div>
    </div>
</body>
</html>
