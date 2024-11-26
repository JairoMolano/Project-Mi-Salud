package co.usco.demo.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import co.usco.demo.models.Constants;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.AppointmentService;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;
import co.usco.demo.services.UserService;
import java.nio.file.Path;
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

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale newLocale = Locale.forLanguageTag(lang);
        localeResolver.setLocale(request, response, newLocale);
        return "redirect:" + controllerHelperService.getPreviousPage(request);
    }

    @GetMapping("/upload-document")
    public String uploadDocument(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/upload-document");
        return "medical-staff/upload-document";
    }

    @PostMapping("/upload-document")
    public String uploadDocument(Model model,
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam("documentType") Constants.DocumentType documentType,
                                 @RequestParam("patientId") Long patientId) throws IOException {
        documentService.uploadFile(file, documentType, patientId);
        
        controllerHelperService.addCommonAttributes(model, "/medical-staff/patients");
        model.addAttribute("patient", userService.getUserById(patientId));
        return "medical-staff/patient-information";
    }

    @PostMapping("/upload-document-appointment")
    public String uploadDocumentAppointment(Model model,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam("documentType") Constants.DocumentType documentType,
                                            @RequestParam("appointmentId") Long appointmentId) throws IOException {
        UserModel patient = appointmentService.getPatientByAppointmentId(appointmentId);
        documentService.uploadFile(file, documentType, patient.getId());
        controllerHelperService.addCommonAttributes(model, "/medical-staff/day-appointments");
        model.addAttribute("appointment", appointmentService.getAppointmentById(appointmentId));
        return "medical-staff/attending-appointment";
    }

    @GetMapping("/download/{id}")
    public void downloadDocument(@PathVariable Long id, HttpServletResponse response) throws IOException {
        documentService.downloadDocument(id, response);
    }

    @GetMapping("/user-profile")
    public String profile(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/upload-document");
        UserModel user = userService.getSessionUser();
        model.addAttribute("user", user);
        String roleNames = user.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.joining(","));
        model.addAttribute("roleNames", roleNames);
        return "user-profile";
    }

    @PostMapping("/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file) {
        try {
            UserModel user = userService.getSessionUser();
            String oldProfilePicturePath = user.getProfilePicturePath();
            
            if (oldProfilePicturePath != null && !oldProfilePicturePath.equals("/profile-pictures/profile-picture-default.png")) {
                String oldFileName = oldProfilePicturePath.substring(oldProfilePicturePath.lastIndexOf("/") + 1);
                Path oldFilePath = Paths.get("profile-pictures/" + oldFileName);
                
                Files.deleteIfExists(oldFilePath);
            }
            
            String newFileName = file.getOriginalFilename();
            Path path = Paths.get("profile-pictures/" + newFileName);
            Files.write(path, file.getBytes());

            user.setProfilePicturePath("/profile-pictures/" + newFileName);
            userService.save(user);

            return "redirect:/common/user-profile";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
