package co.usco.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.usco.demo.models.Constants;
import co.usco.demo.models.OrderModel;
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

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

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
    public String scheduleAppointmentForm(Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        return "patient/schedule-appointment/select-type";
    }

    @PostMapping("/appointments/schedule")
    public String scheduleAppointment(@RequestParam Constants.MedicalSpecialty appointmentType, Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");

        if (appointmentType == Constants.MedicalSpecialty.LABORATORY
                || appointmentType == Constants.MedicalSpecialty.SPECIALIST) {

            List<OrderModel> authorizedOrders = appointmentService.getAuthorizedOrdersForAppointmentType(
                    appointmentType, userService.getSessionUser());

            if (authorizedOrders.isEmpty()) {
                model.addAttribute("errorMessage", getMessage("dontHaveOrderAuthorization"));
                return "patient/schedule-appointment/select-type";
            }

            model.addAttribute("appointmentType", appointmentType);
            model.addAttribute("authorizedOrders", authorizedOrders);
            return "patient/schedule-appointment/select-order";
        }

        if (appointmentService.userHasAppointmentOfType(appointmentType)) {
            model.addAttribute("errorMessage", getMessage("alreadyAppointmentScheduled"));
            return "patient/schedule-appointment/select-type";
        }

        model.addAttribute("appointments", appointmentService.getAvailableAppointmentsByType(appointmentType));
        return "patient/schedule-appointment/select-appointment";
    }

    @PostMapping("/appointments/confirm-order")
    public String confirmOrder(@RequestParam Long orderId,
                               @RequestParam Constants.MedicalSpecialty appointmentType,
                               Model model,
                               HttpSession session) {
        controllerHelperService.addCommonAttributes(model, "/patient/appointments");
        OrderModel order = orderService.getOrderById(orderId);
        session.setAttribute("orderId", order.getId());
        model.addAttribute("appointments", appointmentService.getAvailableAppointmentsByType(appointmentType));
        return "patient/schedule-appointment/select-appointment";
    }

    @PostMapping("/appointments/confirm-schedule")
    public String confirmSchedule(@RequestParam Long appointmentId, HttpSession session) {
        appointmentService.confirmAndScheduleAppointment(appointmentId, session);
        return "redirect:/patient/appointments";
    }

    @PostMapping("/appointments/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId) {
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
    public String proceedFromAuthorization(@RequestParam("orderType") String orderType) {
        if ("MEDICATION".equals(orderType)) {
            return "redirect:/patient/medicines";
        } else if ("SPECIALIST_APPOINTMENT".equals(orderType) || "LAB_APPOINTMENT".equals(orderType)) {
            return "redirect:/patient/appointments/schedule";
        } else {
            return "redirect:/patient/appointments";
        }
    }

    @PostMapping("/authorizations/request-authorization")
    public String requestAuthorization(@RequestParam Long orderId) {
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
    public String filterResults(@RequestParam(required = false) String documentType,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        controllerHelperService.addCommonAttributes(model, "/patient/results");
        documentService.addDocumentAttributesForFilteredResults(model, documentType, page, 9);
        return "patient/results";
    }

    @GetMapping("/history")
    public String history(Model model, @RequestParam(defaultValue = "0") int page) {
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
    public String requestMedicine(@RequestParam Long orderId) {
        orderService.markOrderAsInDelivery(orderId);
        return "redirect:/patient/medicines";
    }

}