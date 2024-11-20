package co.usco.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.AuthService;
import co.usco.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String documentType, @RequestParam String documentNumber, HttpSession session, Model model) {
        UserModel user = userService.findByDocumentNumberAndDocumentType(documentNumber, documentType);
        if (user == null) {
            model.addAttribute("error", getMessage("auth.documentInvalid"));
            return "auth/register";
        }
        if (user.isUserActive()) {
            model.addAttribute("error", getMessage("auth.alreadyRegisteredUser"));
            return "auth/register";
        }
        if (authService.isDocumentRegistered(documentType, documentNumber)) {
            String verificationCode = authService.createAndSetVerificationCode(user);
            session.setAttribute("user", user);
            session.setAttribute("verificationCode", verificationCode);
            authService.sendVerificationCode(user.getEmail(), verificationCode);
            return "redirect:/auth/validate-code";
        }
        model.addAttribute("error", getMessage("auth.somethingWentWrong"));
        return "auth/register";
    }

    @GetMapping("/validate-code")
    public String validateCode(HttpSession session, Model model) {
        UserModel user = (UserModel) session.getAttribute("user");
        String maskedEmail = authService.maskEmail(user.getEmail());
        model.addAttribute("maskedEmail", maskedEmail);
        return "auth/validate-code";
    }

    @PostMapping("/validate-code")
    public String validateCode(@RequestParam String code, HttpSession session, Model model) {
        try {
            UserModel user = (UserModel) session.getAttribute("user");
            if (authService.verifyCode(user, code)) {
                return "redirect:/auth/create-password";
            } else {
                model.addAttribute("error", getMessage("auth.verificationCodeInvalid"));
                model.addAttribute("maskedEmail", authService.maskEmail(user.getEmail()));
                return "auth/validate-code";
            }
        } catch (Exception e) {
            model.addAttribute("error", getMessage("auth.somethingWentWrong") + e.getMessage());
            return "auth/validate-code";
        }
    }

    @GetMapping("/create-password")
    public String createPassword(HttpSession session) {
        return "auth/create-password";
    }

    @PostMapping("/create-password")
    public String createPassword(@RequestParam String password, @RequestParam String confirmPassword, HttpSession session, Model model) {
        try {
            if (password.equals(confirmPassword)) {
                UserModel user = (UserModel) session.getAttribute("user");
                user.setPassword(passwordEncoder.encode(password));
                user.setUserActive(true);
                userService.save(user);
                return "auth/success-registration";
            } else {
                model.addAttribute("error", getMessage("auth.passwordsDoNotMatch"));
                return "auth/create-password";
            }
        } catch (Exception e) {
            model.addAttribute("error", getMessage("auth.somethingWentWrong") + e.getMessage());
            return "auth/create-password";
        }
    }

    @GetMapping("/success-registration")
    public String successRegistration() {
        return "auth/success-registration";
    }

}
