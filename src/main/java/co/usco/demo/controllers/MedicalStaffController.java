package co.usco.demo.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import co.usco.demo.models.Constants;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;

@Controller
@RequestMapping("/medical-staff")
public class MedicalStaffController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ControllerHelperService controllerHelperService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/dashboard");
        return "medical-staff/dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/appointments");
        return "medical-staff/appointments";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/patients");
        return "medical-staff/patients";
    }

    @GetMapping("/upload-document")
    public String uploadDocument(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/upload-document");
        return "medical-staff/upload-document";
    }


    @PostMapping("/upload-document")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                 @RequestParam("documentType") Constants.DocumentType documentType,
                                 @RequestParam("patientId") Long patientId) throws IOException {
        documentService.uploadFile(file, documentType, patientId);
        return "redirect:/medical-staff/dashboard";
    }
    
}
