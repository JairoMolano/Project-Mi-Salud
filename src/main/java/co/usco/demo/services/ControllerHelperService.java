package co.usco.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import co.usco.demo.models.UserModel;

@Service
public class ControllerHelperService {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    public void addCommonAttributes(Model model, String currentUri) {
        UserModel user = userService.getSessionUser();
        model.addAttribute("user", user);
        model.addAttribute("currentUri", currentUri);
    }

    public void addUserAppointments(Model model) {
        UserModel user = userService.getSessionUser();
        model.addAttribute("scheduledAppointments", appointmentService.getScheduledAppointmentsByPatient(user));
        model.addAttribute("finishedAppointments", appointmentService.getFinisheddAppointmentsByPatient(user));
    }
}

