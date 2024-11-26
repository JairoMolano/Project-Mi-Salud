package co.usco.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.usco.demo.models.Constants;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.AppointmentService;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;
import co.usco.demo.services.OrderService;
import co.usco.demo.services.UserService;

@Controller
@RequestMapping("/medical-staff")
public class MedicalStaffController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

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
        model.addAttribute("appointments", appointmentService.getAppointmentsByStatusAndDate(Constants.AppointmentStatus.SCHEDULED));
        model.addAttribute("appointmentsCount", appointmentService.getAppointmentsByStatusAndDate(Constants.AppointmentStatus.SCHEDULED).size());
        model.addAttribute("appointmentsCompleted", appointmentService.getAppointmentsByStatusAndDate(Constants.AppointmentStatus.FINISHED).size());
        return "medical-staff/day-appointments";
    }

    @PostMapping("/attending-appointments")
    public String attendingAppointments(Model model, Long appointmentId) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/day-appointments");
        model.addAttribute("appointment", appointmentService.getAppointmentById(appointmentId));
        return "medical-staff/attending-appointment";
    }

    @PostMapping("create-order")
    public String placeOrder(Model model, Long appointmentId, Constants.OrderType orderType, String description) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/day-appointments");
        orderService.createOrder(appointmentId, orderType, description);
        model.addAttribute("appointment", appointmentService.getAppointmentById(appointmentId));
        return "medical-staff/attending-appointment";
    }

    @PostMapping("/finish-appointment")
    public String finishAppointment(Model model, Long appointmentId) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/day-appointments");
        appointmentService.finishAppointment(appointmentId);
        return "redirect:/medical-staff/day-appointments";
    }

    @GetMapping("/all-appointments")
    public String allAppointments(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/all-appointments");
        model.addAttribute("appointments", appointmentService.getScheduledAppointmentsByDoctorExcludingToday());
        return "medical-staff/all-appointments";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/patients");
        model.addAttribute("patients", appointmentService.getPatientsByDoctor());
        return "medical-staff/patients";
    }

    @GetMapping("/patient-search")
    public String searchPatient(@RequestParam("documentNumber") String documentNumber, Model model) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/patients");
        List<UserModel> patients = appointmentService.getPatientsByDoctorAndDocumentNumber(documentNumber);
        model.addAttribute("patients", patients);
        return "medical-staff/patients"; 
    }

    @PostMapping("/patient-information")
    public String patientInformation(Model model, Long patientId) {
        controllerHelperService.addCommonAttributes(model, "/medical-staff/patients");
        model.addAttribute("patient", userService.getUserById(patientId));
        appointmentService.addUserAppointments(model, userService.getUserById(patientId));
        orderService.addUserOrders(model, userService.getUserById(patientId));
        documentService.addDocumentAttributesForCurrentUser(model, 0, 3, userService.getUserById(patientId));
        return "medical-staff/patient-information";
    }
    
}
