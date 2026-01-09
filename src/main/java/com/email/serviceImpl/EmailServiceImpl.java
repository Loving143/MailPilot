package com.email.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.email.exception.BadRequestException;
import com.email.resposne.EmailLogResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.email.constants.EmailConstants;
import com.email.constants.EmailStatus;
import com.email.entity.EmailLog;
import com.email.entity.Person;
import com.email.entity.RecentEmail;
import com.email.repository.EmailLogRepository;
import com.email.repository.PersonRepository;
import com.email.repository.RecentEmailRepository;
import com.email.request.EmailIntentRequest;
import com.email.request.HrDetailsRequest;
import com.email.request.QuickSendRequest;
import com.email.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailLogRepository repository;

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private RecentEmailRepository recentEmailRepo;

    @Value("${resume.path}")
    private String resumePath;

    public void send(HrDetailsRequest req) {
        logger.info("Sending email to: {}", req.getEmail());
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(req.getEmail());
            helper.setSubject(req.getSubject());
            helper.setText(req.getBody(), true);
            FileSystemResource resume =
                    new FileSystemResource(resumePath + "Prateek_Kumar_Resume.pdf");
            helper.addAttachment("Resume.pdf", resume);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", req.getEmail());
        }catch(Exception ex) {
            logger.error("Error sending email to: {}", req.getEmail(), ex);
            throw new BadRequestException("Error while sending email!");
         }
    }

    @Override
    public void updateEmailStatus(String email, EmailStatus status, String mobNo) {
        logger.info("Updating email status for: {} to status: {}", email, status);
        try {
            EmailLog emailLog = repository.findByRecipientEmail(email).orElseThrow(() -> new BadRequestException("Email not found!"));
            emailLog.setStatus(status);
            if (emailLog.getMobNo() == null) {
                emailLog.setMobNo(mobNo);
            }
            repository.save(emailLog);
            logger.info("Email status updated successfully for: {}", email);
        } catch (Exception e) {
            logger.error("Error updating email status for: {}", email, e);
            throw e;
        }
    }

    @Override
    public ByteArrayInputStream generateExcel() throws IOException {
        logger.info("Generating Excel report for email logs");
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Email Logs");

            // ===== Header Style =====
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Mobile No", "Recipient Email",
                    "Sent At", "Subject", "Status"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // ===== Data Rows =====
            List<EmailLog> logs = repository.findAll();
            logger.debug("Found {} email logs for Excel generation", logs.size());
            int rowIdx = 1;
            for (EmailLog log : logs) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(log.getMobNo() != null ? log.getMobNo() : "");
                row.createCell(1).setCellValue(log.getRecipientEmail());
                row.createCell(2).setCellValue(log.getSentAt().toString());
                row.createCell(3).setCellValue(log.getSubject());
                row.createCell(4).setCellValue(log.getStatus() != null ? log.getStatus().getValue() : "");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            logger.info("Excel report generated successfully with {} records", logs.size());
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            logger.error("Error generating Excel report", e);
            throw e;
        }
    }

    @Override
    public void sendExcel(String mail, byte[] excelBytes) throws MessagingException {
        logger.info("Sending Excel report to: {}", mail);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = 
                    new MimeMessageHelper(message, true);

            helper.setFrom("javaninza@gmail.com");
            helper.setTo("Prateek.kumar949@gmail.com");
            helper.setSubject("Email Logs Report");
            helper.setText(
                    "<h3>Email Logs Attached</h3><p>Please find the Excel report.</p>",
                    true
            );

            helper.addAttachment(
                    "email_logs.xlsx",
                    new ByteArrayResource(excelBytes)
            );

            mailSender.send(message);
            logger.info("Excel report sent successfully to: {}", mail);
        } catch (Exception e) {
            logger.error("Error sending Excel report to: {}", mail, e);
            throw e;
        }
    }

    @Override
    public void addHrDetails(HrDetailsRequest req) {
        logger.info("Adding HR details for email: {}", req.getEmail());
        try {
            if(repository.existsByRecipientEmail(req.getEmail())) {
                logger.warn("Email already exists: {}", req.getEmail());
                throw new BadRequestException("Email already exists.Please update status!!");
            }
            EmailLog emailLog = new EmailLog();
            emailLog.setRecipientEmail(req.getEmail());
            emailLog.setMobNo(req.getMobNo());
            emailLog.setName(req.getName());
            emailLog.setCompany(req.getCompany());
            repository.save(emailLog);
            logger.info("HR details added successfully for email: {}", req.getEmail());
        } catch (Exception e) {
            logger.error("Error adding HR details for email: {}", req.getEmail(), e);
            throw e;
        }
    }

	@Override
	public void saveEmailLog(HrDetailsRequest req) {
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Saving email log for recipient: {} by user: {}", req.getEmail(), userName);
        try {
            EmailLog log = new EmailLog();
            if (repository.existsByRecipientEmail(req.getEmail())) {
                logger.warn("Email already exists in logs: {}", req.getEmail());
                throw new BadRequestException("Email already exists.Please update status!!");
            }
            log.setRecipientEmail(req.getEmail());
            log.setSubject(EmailConstants.SUBJECT);
            log.setSentAt(LocalDateTime.now());
            log.setMobNo(req.getMobNo());
            log.setStatus(EmailStatus.EMAIL_SENT);
            
            Optional<Person> person = personRepository.findByEmail(userName);
            Person person1 = null;
            if(person.isEmpty()) {
                person1 = new Person();
                person1.setEmail(userName);
                person1.addEmails(log);
               
            }else {
            person1=person.get();
            person1.addEmails(log);
            }
            person1 = personRepository.save(person1);
            saveRecentEmail(req,person1);
            logger.info("Email log saved successfully for recipient: {}", req.getEmail());
        } catch (Exception e) {
            logger.error("Error saving email log for recipient: {}", req.getEmail(), e);
            throw new BadRequestException("Failed to save email: " + e.getMessage());
        }
	}
	
	public void saveRecentEmail(HrDetailsRequest req,Person person) {
        logger.debug("Saving recent email for person ID: {}", person.getId());
        try {
            Optional<RecentEmail> recentEmail = recentEmailRepo.findByPersonId(person.getId());
            if(recentEmail.isPresent()) {
                RecentEmail email = recentEmail.get();
                email.setBody(req.getBody());
                email.setRecipientEmail(req.getEmail());
                email.setStatus(req.getStatus());
                email.setSubject(req.getSubject());
                recentEmailRepo.save(email);
                logger.debug("Updated existing recent email for person ID: {}", person.getId());
            }else {
            RecentEmail recent = new RecentEmail();
            recent.setRecipientEmail(req.getEmail());
            recent.setBody(req.getBody());
            recent.setSentAt(LocalDateTime.now());
            recent.setStatus(req.getStatus());
            recent.setSubject(req.getSubject());
            recent.setPerson(person);
            recentEmailRepo.save(recent);
            logger.debug("Created new recent email for person ID: {}", person.getId());
            }
        } catch (Exception e) {
            logger.error("Error saving recent email for person ID: {}", person.getId(), e);
            throw e;
        }
	}

	@Override
	public void quickSend(QuickSendRequest req) {
        String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Quick send request for recipient: {} by user: {}", req.getRecipientEmail(), currentUsername);
        try {
            Person person = personRepository.findByEmail(currentUsername).
                                orElseThrow(()->new BadRequestException("Person with email id does not exists!"));
            RecentEmail recentEmail = recentEmailRepo.findByPersonId(person.getId()).orElseThrow(()->new RuntimeException("No recent email found!!"));
            saveEmailLog(req.getRecipientEmail());
            SendEmail(recentEmail,req);
            recentEmail.setRecipientEmail(req.getRecipientEmail());
            recentEmailRepo.save(recentEmail);
            logger.info("Quick send completed successfully for recipient: {}", req.getRecipientEmail());
        } catch (Exception e) {
            logger.error("Error in quick send for recipient: {}", req.getRecipientEmail(), e);
            throw e;
        }
	}

    public void saveEmailLog(String email) {
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Saving email log for recipient: {} by user: {}", email, userName);
        try {
            EmailLog log = new EmailLog();
            if (repository.existsByRecipientEmail(email)) {
                logger.warn("Email already exists in logs: {}", email);
                throw new BadRequestException("Email already exists.Please update status!!");
            }
            log.setRecipientEmail(email);
            log.setSubject(EmailConstants.SUBJECT);
            log.setSentAt(LocalDateTime.now());
            log.setStatus(EmailStatus.EMAIL_SENT);

            Optional<Person> person = personRepository.findByEmail(userName);
            Person person1 = null;
            if(person.isEmpty()) {
                person1 = new Person();
                person1.setEmail(userName);
                person1.addEmails(log);

            }else {
                person1=person.get();
                person1.addEmails(log);
            }
            person1 = personRepository.save(person1);
            logger.debug("Email log saved successfully for recipient: {}", email);
        } catch (Exception e) {
            logger.error("Error saving email log for recipient: {}", email, e);
            throw new BadRequestException("Failed to save email: " + e.getMessage());
        }
    }
	
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
            helper.addAttachment("Resume.pdf", resume);
            mailSender.send(message);
            logger.info("Email sent successfully to: {} using recent template", req.getRecipientEmail());
        }catch(Exception ex) {
            logger.error("Error sending email to: {} using recent template", req.getRecipientEmail(), ex);
            throw new BadRequestException("Error sending email: " + ex.getMessage());
         }
    }

	@Override
	public void sendIntentEmail(EmailIntentRequest req) {
        logger.info("Sending intent email to: {}", req.getEmail());
        try { 
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(req.getEmail());
            helper.setSubject(req.getSubject());
            helper.setText(req.getBody(), true);
            mailSender.send(message);
            logger.info("Intent email sent successfully to: {}", req.getEmail());
        }catch(Exception ex) {
            logger.error("Error sending intent email to: {}", req.getEmail(), ex);
            throw new BadRequestException("Error sending intent email: " + ex.getMessage());
         }
	}

    @Override
    public void sendPasswordResetEmail(String email, String url, String subject) {
        logger.info("Sending password reset email to: {}", email);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(url, true);
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", email);
        }catch(Exception ex) {
            logger.error("Error sending password reset email to: {}", email, ex);
            throw new BadRequestException("Error sending password reset email: " + ex.getMessage());
        }
    }

    @Override
    public List<EmailLogResponse> fetchAllEmails() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Fetching all emails for user: {}", email);
        try {
            Person person = personRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("User not found"));
            List<EmailLog> emails = repository.findByPersonId(person.getId());
            logger.info("Found {} emails for user: {}", emails.size(), email);
            return emails.stream().map(EmailLogResponse::new).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all emails for user: {}", email, e);
            throw e;
        }
    }

    @Override
    public EmailLogResponse fetchEmailById(Long id) {
        logger.info("Fetching email by ID: {}", id);
        try {
            EmailLog emailLog = repository.findById(id)
                    .orElseThrow(() -> new BadRequestException("Email not found"));
            logger.info("Email found with ID: {}", id);
            return new EmailLogResponse(emailLog);
        } catch (Exception e) {
            logger.error("Error fetching email by ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public void deleteEmailById(Long id) {
        logger.info("Deleting email with ID: {}", id);
        try {
            if(!repository.existsById(id)) {
                logger.warn("Email log not found with ID: {}", id);
                throw new BadRequestException("Email log not found");
            }
            repository.deleteById(id);
            logger.info("Email deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting email with ID: {}", id, e);
            throw e;
        }
    }
}
