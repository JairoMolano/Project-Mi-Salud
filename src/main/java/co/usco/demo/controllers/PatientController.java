package co.usco.demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.usco.demo.models.constants.DocumentType;
import co.usco.demo.models.constants.MedicalSpecialty;
import co.usco.demo.services.AppointmentService;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ControllerHelperService controllerHelperService;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/dashboard");
        return "patient/dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        controllerHelperService.addUserAppointments(model);
        return "patient/appointments";
    }

    @GetMapping("/appointments/schedule")
    public String scheduleAppointment(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return "patient/schedule-appointment/select-type";
    }

    @GetMapping("/authorizations")
    public String authorizations(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/authorizations");
        return "patient/authorizations";
    }

    @GetMapping("/results")
    public String results(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        model.addAttribute("documents", documentService.getDocumentsForCurrentUser());
        return "patient/results";
    }

    @PostMapping("/filter-document")
    public String filterDocument(@RequestParam DocumentType documentType, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        if (documentType.equals(DocumentType.LABORATORY_RESULT) || documentType.equals(DocumentType.REDIOGRAPHY) || documentType.equals(DocumentType.OTHER)) {
            model.addAttribute("documents", documentService.getDocumentsForCurrentUserByType(documentType));
            return "patient/results";
        } else {
            model.addAttribute("documents", documentService.getDocumentsForCurrentUser());
            return "patient/results";
        }
    }

    @GetMapping("/download/{id}")
    public void downloadDocument(@PathVariable Long id, HttpServletResponse response) throws IOException {
        documentService.downloadDocument(id, response);
    }

    @GetMapping("/history")
    public String history(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/history");
        return "patient/history";
    }

    @GetMapping("/medicines")
    public String medicines(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/medicines");
        return "patient/medicines";
    }





    

    @PostMapping("/appointments/schedule")
    public String scheduleAppointment(@RequestParam MedicalSpecialty medicalSpecialty, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        model.addAttribute("appointments", appointmentService.getAvailableAppointmentsByType(medicalSpecialty));
        return "patient/schedule-appointment/select-appointment";
    }

    // HACER QUE SOLO SE PUEDA ASIGNAR UNA CITA  DE CADA TIPO A LA VEZ
    @PostMapping("/appointments/schedule/confirm")
    public String confirmAppointment(@RequestParam Long appointmentId, Model model) {
        appointmentService.scheduleAppointment(appointmentId);
        return "redirect:/patient/appointments";
    }

    @PostMapping("/appointments/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId, Model model) {
        appointmentService.cancelAppointment(appointmentId);
        return "redirect:/patient/appointments";
    }
    
}
