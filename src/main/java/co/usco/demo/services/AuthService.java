package co.usco.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de Verificación");
        message.setText("Tu código de verificación es: " + verificationCode);
        mailSender.send(message);
    }
    

    public String getVerificationCodeByDocumentNumber(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber).getVerificationCode();
    }

    public String createAndSetVerificationCode(UserModel user) {
        String verificationCode = String.format("%06d", (int)(Math.random() * 1000000));
            user.setVerificationCode(verificationCode);
            userService.save(user);
        return verificationCode;
    }

}
