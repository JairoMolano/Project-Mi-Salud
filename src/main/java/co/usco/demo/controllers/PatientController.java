package co.usco.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.usco.demo.models.UserModel;
import co.usco.demo.models.constants.MedicalSpecialty;
import co.usco.demo.services.AppointmentService;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.UserService;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ControllerHelperService controllerHelperService;

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
        return "patient/results";
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
