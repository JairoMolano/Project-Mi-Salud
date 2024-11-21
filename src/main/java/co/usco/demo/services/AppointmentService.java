package co.usco.demo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import co.usco.demo.models.AppointmentModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.models.constants.AppointmentStatus;
import co.usco.demo.models.constants.MedicalSpecialty;
import co.usco.demo.repositories.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserService userService;

    public List<AppointmentModel> getScheduledAppointmentsByPatient(UserModel patient) {
        return appointmentRepository.findByStatusAndPatient(AppointmentStatus.SCHEDULED, patient);
    }

    public List<AppointmentModel> getFinisheddAppointmentsByPatient(UserModel patient) {
        return appointmentRepository.findByStatusAndPatient(AppointmentStatus.FINISHED, patient);
    }

    public void addUserAppointments(Model model) {
        UserModel user = userService.getSessionUser();
        model.addAttribute("scheduledAppointments", getScheduledAppointmentsByPatient(user));
        model.addAttribute("finishedAppointments", getFinisheddAppointmentsByPatient(user));
    }

    public void cancelAppointment(Long appointmentId) {
        AppointmentModel appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));
        if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            appointment.setPatient(null);
            appointment.setStatus(AppointmentStatus.AVAILABLE);
            appointmentRepository.save(appointment);
        } else {
            throw new IllegalStateException("Only scheduled appointments can be canceled");
        }
    }

    public List<AppointmentModel> getAvailableAppointmentsByType(MedicalSpecialty medicalSpecialty) {
        return appointmentRepository.findByDoctorMedicalSpecialtyAndStatus(medicalSpecialty, AppointmentStatus.AVAILABLE);
    }

    public void scheduleAppointment(Long appointmentId) {
    UserModel user = userService.getSessionUser();
    AppointmentModel appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));
        if (appointment.getStatus() == AppointmentStatus.AVAILABLE) {
            appointment.setPatient(user);
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointmentRepository.save(appointment);
        } else {
            throw new IllegalStateException("Only available appointments can be scheduled");
        }
    }

    public boolean userHasAppointmentOfType(MedicalSpecialty medicalSpecialty) {
        UserModel user = userService.getSessionUser(); 
        return appointmentRepository.existsByPatientAndDoctorMedicalSpecialtyAndStatus(
                user, medicalSpecialty, AppointmentStatus.SCHEDULED
        );
    }

    public String checkScheduledAppointments(MedicalSpecialty medicalSpecialty, Model model) {
        boolean hasAppointment = userHasAppointmentOfType(medicalSpecialty);
        if (hasAppointment) {
            model.addAttribute("errorMessage", "Ya tienes una cita de este tipo agendada.");
            return "schedule-appointment/select-type";
        } else {
            model.addAttribute("appointments", getAvailableAppointmentsByType(medicalSpecialty));
            return "schedule-appointment/select-appointment";
        }
    }
    


}
