package co.usco.demo.services;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import co.usco.demo.models.UserModel;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    public boolean isDocumentRegistered(String documentType, String documentNumber) {
        return userService.documentExists(documentType, documentNumber);
    }

    public String createAndSetVerificationCode(UserModel user) {
        String verificationCode = String.format("%06d", (int)(Math.random() * 1000000));
            user.setVerificationCode(verificationCode);
            userService.save(user);
        return verificationCode;
    }

    public void sendVerificationCode(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(getMessage("auth.subjectEmail"));
        message.setText(getMessage("auth.messageEmail") + verificationCode);
        mailSender.send(message);
    }

    public String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex <= 3) {
            return email;
        }
        String maskedPart = email.substring(3, atIndex).replaceAll(".", "*");
        return email.substring(0, 3) + maskedPart + email.substring(atIndex);
    }

    public boolean verifyCode(UserModel user, String code) {
        String storedCode = user.getVerificationCode();
        return storedCode != null && storedCode.equals(code);
    }

}
