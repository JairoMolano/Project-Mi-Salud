package co.usco.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medical-staff")
public class MedicalStaffController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "medical-staff/dashboard";
    }
    
}
