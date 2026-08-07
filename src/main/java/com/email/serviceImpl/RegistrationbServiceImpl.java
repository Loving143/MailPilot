package com.email.serviceImpl;

import java.net.InetAddress;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.email.exception.BadRequestException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class RegistrationbServiceImpl implements RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationbServiceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
	private Configuration freemarkerConfig;
    
    @Autowired
    private OtpRepository otpRepo;

    @Override
    @Async
    @Transactional
    public void sendOtp(SendEmailOtpReq req) {
        logger.info("Sending OTP to email: {}", req.getEmail());
        try {
            Optional<Person> person = personRepository.findByEmail(req.getEmail());
            Person p1 = null;
            if(person.isEmpty()){
                p1 = new Person();
                p1.setEmail(req.getEmail());
                logger.debug("Created new person for email: {}", req.getEmail());
            }else{
                p1 = person.get();
                logger.debug("Found existing person for email: {}", req.getEmail());
            }
            Otp otp = generateOtp();
            p1.addOtp(otp);
            sendEmail(req.getEmail(),otp.getOtp()); 
            personRepository.save(p1);
            logger.info("OTP sent successfully to email: {}", req.getEmail());
        } catch (Exception e) {
            logger.error("Error sending OTP to email: {}", req.getEmail(), e);
            throw e;
        }
    }
    

    public Otp generateOtp(){
        logger.debug("Generating new OTP");
        try {
            final SecureRandom random = new SecureRandom();
            String otp = String.format("%06d", random.nextInt(1_000_000));
            Otp newOtp = new Otp();
            newOtp.setCreatedAt(LocalDateTime.now());
            newOtp.setCurrentAttempts(1);
            newOtp.setOtp(otp);
            newOtp.setExpiryAt(LocalDateTime.now().plusMinutes(5));
            logger.debug("OTP generated successfully with expiry: {}", newOtp.getExpiryAt());
            return newOtp;
        } catch (Exception e) {
            logger.error("Error generating OTP", e);
            throw e;
        }
    }

    public void sendEmail(String email,String otp ){
        logger.info("Sending OTP email to: {}", email);
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("name","Prateek");
            model.put("email", email);
            model.put("subscription", "Premium");
            model.put("otp",otp);
            model.put("bankName","Medicare");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(SendOtp.SUBJECT);
            Template template = freemarkerConfig.getTemplate("emailTemplate.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            helper.setText(htmlBody, true);
            JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;
            ClassPathResource logoResource = new ClassPathResource("static/images/mailpilot.png");
            helper.addInline("logoImage", logoResource);
            System.out.println("Host      : " + sender.getHost());
            System.out.println("Port      : " + sender.getPort());
            System.out.println("Username  : " + sender.getUsername());
            mailSender.send(message);
            System.out.println("Host      : " + sender.getHost());
            System.out.println("Port      : " + sender.getPort());
            System.out.println("Username  : " + sender.getUsername());
            logger.info("OTP email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Error sending OTP email to: {}", email, e);
            throw new BadRequestException("Failed to send email: " + e.getMessage());
        }
    }

    @Override
    public void verifyOtp(VerifyOtpRequest req) {
        logger.info("Verifying OTP for email: {}", req.getEmail());
        try {
            Person person = personRepository.findByEmail(req.getEmail()).orElseThrow(()-> new BadRequestException("Otp with email not found!!"));
            Otp otp = otpRepo.findValidOtp(req.getOtp(),req.getEmail()).
                        orElseThrow(()->new RuntimeException("OTP expired or not found !!"));
            LocalDateTime expiryTime = otp.getExpiryAt();
            LocalDateTime currentTime = LocalDateTime.now();
            
            if (!otp.getOtp().equals(req.getOtp())) {
                logger.warn("Incorrect OTP provided for email: {}", req.getEmail());
                throw new BadRequestException("Incorrect OTP");
            }

            long diffInMinutes = Duration.between(expiryTime, currentTime).toMinutes();

            if (diffInMinutes > 5) {
                logger.warn("Expired OTP used for email: {}", req.getEmail());
                throw new BadRequestException("OTP expired");
            }
            otp.setUsed(true);
            otpRepo.save(otp);
            logger.info("OTP verified successfully for email: {}", req.getEmail());
        } catch (Exception e) {
            logger.error("Error verifying OTP for email: {}", req.getEmail(), e);
            throw e;
        }
    }

    @Override
    public boolean verifyOtp(String email, String otpp) {
        logger.info("Verifying OTP for email: {} (boolean method)", email);
        try {
            Person person = personRepository.findByEmail(email).orElseThrow(()-> new BadRequestException("Otp with email not found!!"));
            logger.debug("OTP verification attempt for email: {} with OTP: {}", email, otpp);
            Optional<Otp> otpOptional = otpRepo.findValidOtp(otpp,email);
            logger.debug("OTP lookup result: {}", otpOptional.isPresent());
            
            if (otpOptional.isEmpty()) {
                logger.warn("No valid OTP found for email: {}", email);
                return false;
            }
            
            Otp otp = otpOptional.get();
            LocalDateTime expiryTime = otp.getExpiryAt();
            LocalDateTime currentTime = LocalDateTime.now();

            if (!otp.getOtp().equals(otpp)) {
                logger.warn("Incorrect OTP provided for email: {}", email);
                return false;
            }

            long diffInMinutes = Duration.between(expiryTime, currentTime).toMinutes();

            if (diffInMinutes > 5) {
                logger.warn("Expired OTP used for email: {}", email);
                return false;
            }
            otpRepo.save(otp);
            logger.info("OTP verified successfully for email: {} (boolean method)", email);
            return true;
        } catch (Exception e) {
            logger.error("Error verifying OTP for email: {} (boolean method)", email, e);
            return false;
        }
    }
}
