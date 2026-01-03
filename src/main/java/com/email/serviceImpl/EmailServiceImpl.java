package com.email.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.email.resposne.EmailLogResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        }catch(Exception ex) {
        	 
         }

    }

    @Override
    public void updateEmailStatus(String email, EmailStatus status, String mobNo) {
        EmailLog emailLog = repository.findByRecipientEmail(email).orElseThrow(() -> new RuntimeException("Email not found!"));
        emailLog.setStatus(status);
        if (emailLog.getMobNo() == null) {
            emailLog.setMobNo(mobNo);
        }
        repository.save(emailLog);
    }

    @Override
    public ByteArrayInputStream generateExcel() throws IOException {

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

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public void sendExcel(String mail, byte[] excelBytes) throws MessagingException {
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
    }

    @Override
    public void addHrDetails(HrDetailsRequest req) {
        if(repository.existsByRecipientEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists.Please update status!!");
        }
        EmailLog emailLog = new EmailLog();
        emailLog.setRecipientEmail(req.getEmail());
        emailLog.setMobNo(req.getMobNo());
        emailLog.setName(req.getName());
        emailLog.setCompany(req.getCompany());
        repository.save(emailLog);
    }

	@Override
	public void saveEmailLog(HrDetailsRequest req) {
		try {
		EmailLog log = new EmailLog();
        if (repository.existsByRecipientEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists.Please update status!!");
        }
        log.setRecipientEmail(req.getEmail());
        log.setSubject(EmailConstants.SUBJECT);
        log.setSentAt(LocalDateTime.now());
        log.setMobNo(req.getMobNo());
        log.setStatus(EmailStatus.EMAIL_SENT);
        
        Optional<Person> person = personRepository.findByEmail(req.getPersonEmail());
        Person person1 = null;
        if(person.isEmpty()) {
            person1 = new Person();
            person1.setEmail(req.getPersonEmail());
            person1.addEmails(log);
           
        }else {
        person1=person.get();
        person1.addEmails(log);
        }
        person1 = personRepository.save(person1);
        saveRecentEmail(req,person1);
	} catch (Exception e) {
        throw new RuntimeException("Failed to save email: " + e.getMessage(), e);
		}
	}
	
	public void saveRecentEmail(HrDetailsRequest req,Person person) {
		Optional<RecentEmail> recentEmail = recentEmailRepo.findByPersonId(person.getId());
		if(recentEmail.isPresent()) {
			RecentEmail email = recentEmail.get();
			email.setBody(req.getBody());
			email.setRecipientEmail(req.getEmail());
			email.setStatus(req.getStatus());
			email.setSubject(req.getSubject());
			recentEmailRepo.save(email);
		}else {
		RecentEmail recent = new RecentEmail();
        recent.setRecipientEmail(req.getEmail());
        recent.setBody(req.getBody());
        recent.setSentAt(LocalDateTime.now());
        recent.setStatus(req.getStatus());
        recent.setSubject(req.getSubject());
        recent.setPerson(person);
        recentEmailRepo.save(recent);
		}
	}

	@Override
	public void quickSend(QuickSendRequest req) {
		Person person = personRepository.findByEmail(req.getPersonEmail()).
							orElseThrow(()->new RuntimeException("Person with email id does not exists!"));
		RecentEmail recentEmail = recentEmailRepo.findByPersonId(person.getId()).orElseThrow(()->new RuntimeException("No recent email found!!"));
		SendEmail(recentEmail,req);
		recentEmail.setRecipientEmail(req.getRecipientEmail());
		recentEmailRepo.save(recentEmail);
	}
	
	public void SendEmail(RecentEmail recentEmail,QuickSendRequest req) {
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
	        }catch(Exception ex) {
	        	 
	         }
		}

	@Override
	public void sendIntentEmail(EmailIntentRequest req) {
		 try { 
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setTo(req.getEmail());
	            helper.setSubject(req.getSubject());
	            helper.setText(req.getBody(), true);
//	            FileSystemResource resume = 
//	                    new FileSystemResource(resumePath + "Prateek_Kumar_Resume.pdf");
//	            helper.addAttachment("Resume.pdf", resume);
	            mailSender.send(message);
	        }catch(Exception ex) {
	        	 
	         }
	}

    @Override
    public void sendPasswordResetEmail(String email, String url, String subject) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(url, true);
            mailSender.send(message);
        }catch(Exception ex) {

        }
    }

    @Override
    public List<EmailLogResponse> fetchAllEmails() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<EmailLog> emails = repository.findByPersonId(person.getId());
        return emails.stream().map(EmailLogResponse::new).collect(Collectors.toList());
    }

    @Override
    public EmailLogResponse fetchEmailById(Long id) {
        EmailLog emailLog = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email not found"));
        return new EmailLogResponse(emailLog);
    }

    @Override
    public void deleteEmailById(Long id) {
        if(!repository.existsById(id)) {
            throw new RuntimeException("Email log not found");
        }
        repository.deleteById(id);
    }


}
