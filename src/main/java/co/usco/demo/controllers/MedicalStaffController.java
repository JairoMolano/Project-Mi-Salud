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
import co.usco.demo.services.AppointmentService;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;
import co.usco.demo.services.OrderService;

@Controller
@RequestMapping("/medical-staff")
public class MedicalStaffController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ControllerHelperService controllerHelperService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/dashboard");
        return "medical-staff/dashboard";
    }

    @GetMapping("/day-appointments")
    public String dayAppointments(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/day-appointments");
        model.addAttribute("appointments", appointmentService.getScheduledAppointmentsByDoctorAndDate());
        model.addAttribute("appointmentsCount", appointmentService.getScheduledAppointmentsByDoctorAndDate().size());
        model.addAttribute("appointmentsCompleted", appointmentService.getAppointmentsByStatus(Constants.AppointmentStatus.FINISHED).size());
        return "medical-staff/day-appointments";
    }

    @PostMapping("/attending-appointments")
    public String attendingAppointments(Model model, Long appointmentId) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/day-appointments");
        model.addAttribute("appointmentId", appointmentId);
        return "medical-staff/attending-appointment";
    }

    @PostMapping("create-order")
    public String placeOrder(Model model, Long appointmentId, Constants.OrderType orderType, String description) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/day-appointments");
        orderService.createOrder(appointmentId, orderType, description);
        return "medical-staff/attending-appointment";
    }

    @GetMapping("/all-appointments")
    public String allAppointments(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/all-appointments");
        model.addAttribute("appointments", appointmentService.getAppointmentsByStatus(Constants.AppointmentStatus.SCHEDULED));
        return "medical-staff/all-appointments";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/patients");
        model.addAttribute("patients", appointmentService.getPatientsByDoctor());
        return "medical-staff/patients";
    }

    @GetMapping("/horary")
    public String workingDay(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/horary");
        return "medical-staff/horary";
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
