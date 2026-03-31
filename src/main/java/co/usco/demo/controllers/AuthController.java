package co.usco.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import co.usco.demo.models.AppException;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String documentType,
                               @RequestParam String documentNumber,
                               HttpSession session,
                               Model model) {
        try {
            String maskedEmail = authService.prepareRegistration(documentType, documentNumber, session);
            model.addAttribute("maskedEmail", maskedEmail);
            return "auth/validate-code";
        } catch (AppException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/validate-code")
    public String validateCode(@RequestParam String code, HttpSession session, Model model) {
        try {
            authService.validateCode(code, session);
            return "auth/create-password";
        } catch (AppException e) {
            UserModel user = (UserModel) session.getAttribute("user");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("maskedEmail", authService.maskEmail(user.getEmail()));
            return "auth/validate-code";
        }
    }

    @PostMapping("/create-password")
    public String createPassword(@RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        try {
            authService.finalizeRegistration(password, confirmPassword, session);
            return "auth/success-registration";
        } catch (AppException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/create-password";
        }
    }

}