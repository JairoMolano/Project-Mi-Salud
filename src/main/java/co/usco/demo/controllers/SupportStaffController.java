package co.usco.demo.controllers;

import java.util.List;

import javax.print.Doc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import co.usco.demo.models.Constants;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.AppointmentService;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;
import co.usco.demo.services.OrderService;
import co.usco.demo.services.UserService;

@Controller
@RequestMapping("/support-staff")
public class SupportStaffController {

    @Autowired
    private ControllerHelperService controllerHelperService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        controllerHelperService.addCommonAttributes(model, "/support-staff/dashboard");
        return "support-staff/dashboard";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        controllerHelperService.addCommonAttributes(model, "/support-staff/patients");
        model.addAttribute("patients", userService.getAllUsersByRol("ROLE_PATIENT"));
        return "support-staff/patients";
    }

    @GetMapping("/patient-search")
    public String searchPatient(@RequestParam("documentNumber") String documentNumber, Model model) {
        controllerHelperService.addCommonAttributes(model, "/support-staff/patients");
        List<UserModel> patients = userService.getUsersByRolAndDocumentNumber("ROLE_PATIENT", documentNumber);
        model.addAttribute("patients", patients);
        return "support-staff/patients"; 
    }

    @PostMapping("/patient-information")
    public String patientInformation(Model model, Long patientId) {
        controllerHelperService.addCommonAttributes(model, "/support-staff/patients");
        model.addAttribute("patient", userService.getUserById(patientId));
        model.addAttribute("pendingOrders", orderService.getPendingOrdersByPatientId(patientId));
        model.addAttribute("patient", userService.getUserById(patientId));
        appointmentService.addUserAppointments(model, userService.getUserById(patientId));
        orderService.addUserOrders(model, userService.getUserById(patientId));
        documentService.addDocumentAttributesForCurrentUser(model, 0, 3, userService.getUserById(patientId));
        return "support-staff/patient-information";
    }

    @PostMapping("/update-patient")
    public String updatePatient(@RequestParam Long patientId,
                                @RequestParam String email,
                                @RequestParam String phoneNumber,
                                @RequestParam String address,
                                @RequestParam String department,
                                @RequestParam String city,
                                Model model) {
        UserModel updatedUser = UserModel.builder()
                .id(patientId)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .department(department)
                .city(city)
                .build();

        userService.updateUser(updatedUser);
        model.addAttribute("patient", userService.getUserById(patientId));
        controllerHelperService.addCommonAttributes(model, "/support-staff/patients");
        model.addAttribute("patient", userService.getUserById(patientId));
        appointmentService.addUserAppointments(model, userService.getUserById(patientId));
        orderService.addUserOrders(model, userService.getUserById(patientId));
        documentService.addDocumentAttributesForCurrentUser(model, 0, 3, userService.getUserById(patientId));
        return "support-staff/patient-information";
    }
    
    @GetMapping("/authorizations")
    public String authorizations(Model model) {
        controllerHelperService.addCommonAttributes(model, "/support-staff/authorizations");
        model.addAttribute("pendingOrders", orderService.getAllOrdersByStatus(Constants.OrderStatus.PENDING));
        return "support-staff/authorizations";
    }

    @PostMapping("/authorize-order")
    public String authorizeOrder(Model model, Long orderId) {
        controllerHelperService.addCommonAttributes(model, "/support-staff/authorizations");
        orderService.authorizeOrder(orderId);
        return "redirect:/support-staff/authorizations";
    }

}
