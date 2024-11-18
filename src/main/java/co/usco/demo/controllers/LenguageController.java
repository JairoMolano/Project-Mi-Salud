package co.usco.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class LenguageController {

    private final LocaleResolver localeResolver;

    public LenguageController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale newLocale = Locale.forLanguageTag(lang);
        localeResolver.setLocale(request, response, newLocale);
    
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/home/MiSalud"; 
        }
        return "redirect:" + referer;
    }

}

