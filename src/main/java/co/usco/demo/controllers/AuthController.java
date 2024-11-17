package co.usco.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.AuthService;
import co.usco.demo.services.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String documentType, @RequestParam String documentNumber, HttpSession session) {

        UserModel user = userService.findByDocumentNumberAndDocumentType(documentNumber, documentType);

        if (user.isUserActive()) {
            return "redirect:/auth/login";
        }

        if (userService.documentExists(documentType, documentNumber)) {
            
            // Create verification code
            String verificationCode = authService.createAndSetVerificationCode(user);

            // Save user's documentNumber and verificationCode in the session
            session.setAttribute("documentNumber", documentNumber);
            session.setAttribute("verificationCode", verificationCode);

            // Sent verification code
            authService.sendVerificationCode(user.getEmail(), verificationCode);
            return "redirect:/auth/validate-code";

        } else {

            System.out.println("Usuario no existe");
            return "auth/register";

        }
    }

    @GetMapping("/validate-code")
    public String validateCode() {
        return "auth/validate-code";
    }

    @PostMapping("/validate-code")
    public String validateCode(@RequestParam String code, HttpSession session) {

        String documentNumber = (String) session.getAttribute("documentNumber");
        String verificationCode = (String) session.getAttribute("verificationCode");
        UserModel user = userService.findByDocument(documentNumber);

        if (code.equals(verificationCode)) {
            user.setUserActive(true);
            userService.save(user);
            return "redirect:/auth/login";
        } else {
            return "auth/validate-code";
        }
    }

}

