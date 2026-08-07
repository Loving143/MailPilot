package com.email.serviceImpl;

import com.email.entity.RecentEmail;
import com.email.exception.BadRequestException;
import com.email.request.QuickSendRequest;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailAsyncServiceImpl implements EmailAsyncService{
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private JavaMailSender mailSender;
    @Value("${resume.path}")
    private String resumePath;
    @Override
    @Async
    public void SendEmail(RecentEmail recentEmail,QuickSendRequest req) {
        logger.info("Sending email to: {} with recent email template", req.getRecipientEmail());
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(req.getRecipientEmail());
            helper.setSubject(recentEmail.getSubject());
            helper.setText(recentEmail.getBody(), true);
            FileSystemResource resume =
                    new FileSystemResource(resumePath + "Prateek_Kumar_Resume.pdf");
            helper.addAttachment("Prateek_Kumar_Resume.pdf", resume);
            mailSender.send(message);
            logger.info("Email sent successfully to: {} using recent template", req.getRecipientEmail());
        }catch(Exception ex) {
            logger.error("Error sending email to: {} using recent template", req.getRecipientEmail(), ex);
            throw new BadRequestException("Error sending email: " + ex.getMessage());
        }
    }
}
