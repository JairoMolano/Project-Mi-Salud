package co.usco.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/MiSalud")
    public String home() {
        return "pages/home";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "pages/contacts";
    }

}
