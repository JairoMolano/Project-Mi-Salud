package co.usco.demo.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import co.usco.demo.models.constants.DocumentType;
import co.usco.demo.services.DocumentService;

@Controller
@RequestMapping("/medical-staff")
public class MedicalStaffController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "medical-staff/dashboard";
    }

    @PostMapping("/upload-document")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                 @RequestParam("documentType") DocumentType documentType,
                                 @RequestParam("patientId") Long patientId) throws IOException {
        documentService.uploadFile(file, documentType, patientId);
        return "redirect:/medical-staff/dashboard";
    }
    
}
