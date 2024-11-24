package co.usco.demo.controllers;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private ControllerHelperService controllerHelperService;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale newLocale = Locale.forLanguageTag(lang);
        localeResolver.setLocale(request, response, newLocale);
        return "redirect:" + controllerHelperService.getPreviousPage(request);
    }

    @GetMapping("/download/{id}")
    public void downloadDocument(@PathVariable Long id, HttpServletResponse response) throws IOException {
        documentService.downloadDocument(id, response);
    }

    @GetMapping("/user-profile")
    public String profile(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/profile");
        return "user-profile";
    }
}
