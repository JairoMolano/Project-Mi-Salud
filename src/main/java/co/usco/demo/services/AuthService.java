package co.usco.demo.services;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import co.usco.demo.models.AppException;
import co.usco.demo.models.UserModel;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    public String prepareRegistration(String documentType, String documentNumber, HttpSession session) {
        UserModel user = userService.findByDocumentNumberAndDocumentType(documentNumber, documentType);

        if (user == null) {
            throw new AppException(getMessage("auth.documentInvalid"));
        }
        if (user.isUserActive()) {
            throw new AppException(getMessage("auth.alreadyRegisteredUser"));
        }
        if (!isDocumentRegistered(documentType, documentNumber)) {
            throw new AppException(getMessage("auth.somethingWentWrong"));
        }

        String verificationCode = createAndSetVerificationCode(user);
        session.setAttribute("user", user);
        sendVerificationCode(user.getEmail(), verificationCode);
        return maskEmail(user.getEmail());
    }

    public void validateCode(String code, HttpSession session) {
        UserModel user = (UserModel) session.getAttribute("user");
        if (!verifyCode(user, code)) {
            throw new AppException(getMessage("auth.verificationCodeInvalid"));
        }
    }

    public void finalizeRegistration(String password, String confirmPassword, HttpSession session) {
        if (!password.equals(confirmPassword)) {
            throw new AppException(getMessage("auth.passwordsDoNotMatch"));
        }
        UserModel user = (UserModel) session.getAttribute("user");
        user.setPassword(passwordEncoder.encode(password));
        user.setUserActive(true);
        userService.save(user);
    }

    public boolean isDocumentRegistered(String documentType, String documentNumber) {
        return userService.documentExists(documentType, documentNumber);
    }

    public String createAndSetVerificationCode(UserModel user) {
        String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));
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
