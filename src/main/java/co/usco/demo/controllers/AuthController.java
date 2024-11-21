package co.usco.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
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
    public String registerUser(@RequestParam String documentType, @RequestParam String documentNumber, HttpSession session, Model model) {
        return "auth/" + authService.registerUser(documentType, documentNumber, session, model);
    }

    @PostMapping("/validate-code")
    public String validateCode(@RequestParam String code, HttpSession session, Model model) {
        return "auth/" + authService.validateCode(code, session, model);
    }

    @PostMapping("/create-password")
    public String createPassword(@RequestParam String password, @RequestParam String confirmPassword, HttpSession session, Model model) {
        return "auth/" + authService.createPassword(password, confirmPassword, session, model);
    }

}
