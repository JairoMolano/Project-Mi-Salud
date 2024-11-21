package co.usco.demo.services;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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

    public String registerUser(String documentType, String documentNumber, HttpSession session, Model model) {
        UserModel user = userService.findByDocumentNumberAndDocumentType(documentNumber, documentType);
        if (user == null) {
            model.addAttribute("error", getMessage("auth.documentInvalid"));
            return "register";
        }
        if (user.isUserActive()) {
            model.addAttribute("error", getMessage("auth.alreadyRegisteredUser"));
            return "register";
        }
        if (isDocumentRegistered(documentType, documentNumber)) {
            String verificationCode = createAndSetVerificationCode(user);
            session.setAttribute("user", user);
            session.setAttribute("verificationCode", verificationCode);
            sendVerificationCode(user.getEmail(), verificationCode);
            model.addAttribute("maskedEmail", maskEmail(user.getEmail()));
            return "validate-code";
        } else {
            model.addAttribute("error", getMessage("auth.somethingWentWrong"));
            return "register";
        }
    }

    public String validateCode(String code, HttpSession session, Model model) {
        UserModel user = (UserModel) session.getAttribute("user");
        if (verifyCode(user, code)) {
            return "create-password";
        } else {
            model.addAttribute("error", getMessage("auth.verificationCodeInvalid"));
            model.addAttribute("maskedEmail", maskEmail(user.getEmail()));
            return "validate-code";
        }
    }

    public String createPassword(String password, String confirmPassword, HttpSession session, Model model) {
        if (password.equals(confirmPassword)) {
            UserModel user = (UserModel) session.getAttribute("user");
            user.setPassword(passwordEncoder.encode(password));
            user.setUserActive(true);
            userService.save(user);
            return "success-registration";
        } else {
            model.addAttribute("error", getMessage("auth.passwordsDoNotMatch"));
            return "create-password";
        }
    }

}
