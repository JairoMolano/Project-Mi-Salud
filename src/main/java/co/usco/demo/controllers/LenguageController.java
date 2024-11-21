package co.usco.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import co.usco.demo.services.ControllerHelperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class LenguageController {

    @Autowired
    private ControllerHelperService controllerHelperService;

    @Autowired
    private LocaleResolver localeResolver;

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale newLocale = Locale.forLanguageTag(lang);
        localeResolver.setLocale(request, response, newLocale);
        return "redirect:" + controllerHelperService.getPreviousPage(request);
    }

}

