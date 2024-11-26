package co.usco.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.usco.demo.models.Constants;
import co.usco.demo.services.AppointmentService;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.DocumentService;
import co.usco.demo.services.OrderService;
import co.usco.demo.services.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ControllerHelperService controllerHelperService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/dashboard");
        return "patient/dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        appointmentService.addUserAppointments(model, userService.getSessionUser());
        return "patient/appointments";
    }

    @GetMapping("/appointments/schedule")
    public String scheduleAppointment(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return "patient/schedule-appointment/select-type";
    }

    @PostMapping("/appointments/schedule")
    public String scheduleAppointment(@RequestParam Constants.MedicalSpecialty appointmentType, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return appointmentService.handleScheduleAppointment(appointmentType, model);
    }

    @PostMapping("/appointments/confirm-order")
    public String confirmAppointment(@RequestParam Long orderId, @RequestParam Constants.MedicalSpecialty appointmentType, Model model, HttpSession session) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return appointmentService.handleConfirmAppointmentOrder(orderId, appointmentType, model, session);
    }

    @PostMapping("/appointments/confirm-schedule")
    public String confirmAppointment(@RequestParam Long appointmentId, Model model, HttpSession session) {
        appointmentService.confirmAndScheduleAppointment(appointmentId, session);
        return "redirect:/patient/appointments";
    }

    @PostMapping("/appointments/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId, Model model) {
        System.out.println("Canceling appointment with id: " + appointmentId);
        appointmentService.cancelAppointmentAndHandleOrder(appointmentId);
        return "redirect:/patient/appointments";
    }

    @GetMapping("/authorizations")
    public String authorizations(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/authorizations");
        orderService.addUserOrders(model, userService.getSessionUser());
        return "patient/authorizations";
    }

    @PostMapping("/authorizations/proceed")
    public String proceedToAppointment(@RequestParam("orderType") String orderType, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return orderService.getRedirectionPathBasedOnOrderType(orderType);
    }

    @PostMapping("/authorizations/request-authorization")
    public String requestAuthorization(@RequestParam Long orderId, Model model) {
        orderService.markOrderAsPending(orderService.getOrderById(orderId));
        return "redirect:/patient/authorizations";
    }

    @GetMapping("/results")
    public String results(Model model, @RequestParam(defaultValue = "0") int page) {
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        documentService.addDocumentAttributesForCurrentUser(model, page, 9, userService.getSessionUser());
        return "patient/results";
    }

    @GetMapping("/filter-results")
    public String filterDocument(@RequestParam(required = false) String documentType, @RequestParam(defaultValue = "0") int page, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        documentService.addDocumentAttributesForFilteredResults(model, documentType, page, 9);
        return "patient/results";
    }

    @GetMapping("/history")
    public String history(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String documentType) {
        controllerHelperService.addCommonAttributes(model, "/patient/history");
        appointmentService.addUserAppointments(model, userService.getSessionUser());
        orderService.addUserOrders(model, userService.getSessionUser());
        documentService.addDocumentAttributesForCurrentUser(model, page, 3, userService.getSessionUser());
        return "patient/history";
    }

    @GetMapping("/medicines")
    public String medicines(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/medicines");
        orderService.addMedicationOrders(model);
        return "patient/medicines";
    }

    @PostMapping("/medicines/request")
    public String requestMedicine(@RequestParam Long orderId, Model model) {
        orderService.markOrderAsInDelivery(orderId);
        return "redirect:/patient/medicines";
    }
    
}
