package co.usco.demo.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        appointmentService.addUserAppointments(model);
        return "patient/appointments";
    }

    @GetMapping("/appointments/schedule")
    public String scheduleAppointment(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return "patient/schedule-appointment/select-type";
    }

    @PostMapping("/appointments/schedule")
    public String scheduleAppointment(@RequestParam MedicalSpecialty medicalSpecialty, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return "patient/" + appointmentService.checkScheduledAppointments(medicalSpecialty, model);
    }

    @PostMapping("/appointments/confirm-schedule")
    public String confirmAppointment(@RequestParam Long appointmentId, Model model) {
        appointmentService.scheduleAppointment(appointmentId);
        return "redirect:/patient/appointments";
    }

    @PostMapping("/appointments/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId, Model model) {
        appointmentService.cancelAppointment(appointmentId);
        return "redirect:/patient/appointments";
    }

    @GetMapping("/authorizations")
    public String authorizations(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/authorizations");
        return "patient/authorizations";
    }

    @GetMapping("/results")
    public String results(Model model, @RequestParam(defaultValue = "0") int page) {
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        documentService.addDocumentAttributesForCurrentUser(model, page, 9);
        return "patient/results";
    }

    @GetMapping("/filter-results")
    public String filterDocument(@RequestParam(required = false) String documentType, @RequestParam(defaultValue = "0") int page, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        documentService.addDocumentAttributesForFilteredResults(model, documentType, page, 9);
        return "patient/results";
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
    
}
