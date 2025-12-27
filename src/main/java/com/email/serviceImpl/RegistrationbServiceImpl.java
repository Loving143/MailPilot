package com.email.serviceImpl;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.email.constants.SendOtp;
import com.email.entity.Otp;
import com.email.entity.Person;
import com.email.repository.OtpRepository;
import com.email.repository.PersonRepository;
import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;
import com.email.service.RegistrationService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;

@Service
public class RegistrationbServiceImpl implements RegistrationService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
	private Configuration freemarkerConfig;
    
    @Autowired
    private OtpRepository otpRepo;

    @Override
    public void sendOtp(SendEmailOtpReq req) {
        Optional<Person> person = personRepository.findByEmail(req.getEmail());
        Person p1 = null;
        if(person.isEmpty()){
            p1 = new Person();
            p1.setEmail(req.getEmail());
        }else{
            p1 = person.get();
        }
        Otp otp = generateOtp();
        p1.addOtp(otp);
        sendEmail(req.getEmail(),otp.getOtp()); 
        personRepository.save(p1);
    }
    

    public Otp generateOtp(){
        final SecureRandom random = new SecureRandom();
        String otp = String.format("%06d", random.nextInt(1_000_000));
        Otp newOtp = new Otp();
        newOtp.setCreatedAt(LocalDateTime.now());
        newOtp.setCurrentAttempts(1);
        newOtp.setOtp(otp);
        newOtp.setExpiryAt(LocalDateTime.now().plusMinutes(5));
        return newOtp;
    }

    public void sendEmail(String email,String otp ){
    	Map<String, Object> model = new HashMap<>();
        model.put("name","Ishika");
        model.put("email", email);
        model.put("subscription", "Premium");
        model.put("otp",otp);
        model.put("bankName","Medicare");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(SendOtp.SUBJECT);
            Template template = freemarkerConfig.getTemplate("emailTemplate.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            helper.setText(htmlBody, true);
            ClassPathResource logoResource = new ClassPathResource("static/images/medicare.jpg");
            helper.addInline("logoImage", logoResource);
            mailSender.send(message);
		 } catch (Exception e) {
	            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
	        }
    }

    @Override
    public void verifyOtp(VerifyOtpRequest req) {
    	Person person = personRepository.findByEmail(req.getEmail()).orElseThrow(()-> new RuntimeException("Otp with email not found!!"));
		Otp otp = otpRepo.findValidOtp(req.getOtp(),req.getEmail()).
					orElseThrow(()->new RuntimeException("OTP expired or not found !!"));
		LocalDateTime expiryTime = otp.getExpiryAt();
		LocalDateTime currentTime = LocalDateTime.now();
		
		if (!otp.getOtp().equals(req.getOtp())) {
	        throw new RuntimeException("Incorrect OTP");
	    }

	    long diffInMinutes = Duration.between(expiryTime, currentTime).toMinutes();

	    if (diffInMinutes > 5) {
	        throw new RuntimeException("OTP expired");
	    }
	    otp.setUsed(true);
	    otpRepo.save(otp);
		
    }
}
