package co.usco.demo.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import co.usco.demo.models.AppointmentModel;
import co.usco.demo.models.Constants;
import co.usco.demo.models.OrderModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.AppointmentRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    public void addUserAppointments(Model model, UserModel user) {
        model.addAttribute("scheduledAppointments", getScheduledAppointmentsByPatient(user));
        model.addAttribute("finishedAppointments", getFinishedAppointmentsByPatient(user));
    }

    public List<OrderModel> getAuthorizedOrdersForAppointmentType(Constants.MedicalSpecialty appointmentType, UserModel user) {
        Constants.OrderType orderType = appointmentType == Constants.MedicalSpecialty.LABORATORY
                ? Constants.OrderType.LAB_APPOINTMENT
                : Constants.OrderType.SPECIALIST_APPOINTMENT;
        return orderService.getOrdersByTypeAndPatientAndAuthorized(orderType, user);
    }

    public void confirmAndScheduleAppointment(Long appointmentId, HttpSession session) {
        Long orderId = (Long) session.getAttribute("orderId");
        scheduleAppointment(appointmentId);
        if (orderId == null) {
            return;
        }
        OrderModel order = orderService.getOrderById(orderId);
        AppointmentModel appointment = getAppointmentById(appointmentId);
        if (isDoctorLaboratoryOrSpecialist(appointmentId)) {
            orderService.markOrderAsCompleted(order);
            appointment.setOrder(order);
            appointmentRepository.save(appointment);
        }
    }

    public void cancelAppointmentAndHandleOrder(Long appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        if (appointment.getOrder() != null) {
            orderService.markOrderAsAuthorized(appointment.getOrder());
        }
        cancelAppointment(appointmentId);
    }

    public List<AppointmentModel> getAppointmentsByStatusAndDate(Constants.AppointmentStatus status) {
        UserModel doctor = userService.getSessionUser();
        LocalDate today = LocalDate.now();
        return appointmentRepository.findByStatusAndDoctorAndDate(status, doctor, today, Sort.by(Sort.Direction.ASC, "date"));
    }

    public List<AppointmentModel> getAllScheduledAppointmentsByDoctor() {
        UserModel doctor = userService.getSessionUser();
        return appointmentRepository.findByStatusAndDoctor(Constants.AppointmentStatus.SCHEDULED, doctor,
                Sort.by(Sort.Direction.ASC, "date"));
    }

    public List<AppointmentModel> getScheduledAppointmentsByDoctorExcludingToday() {
        UserModel doctor = userService.getSessionUser();
        LocalDate today = LocalDate.now();
        return appointmentRepository.findByStatusAndDoctorAndDateNot(Constants.AppointmentStatus.SCHEDULED, doctor, today,
                Sort.by(Sort.Direction.ASC, "date"));
    }

    public List<UserModel> getPatientsByDoctor() {
        UserModel doctor = userService.getSessionUser();
        return appointmentRepository.findDistinctPatientsByDoctor(doctor);
    }

    public List<UserModel> getPatientsByDoctorAndDocumentNumber(String documentNumber) {
        UserModel doctor = userService.getSessionUser();
        return appointmentRepository.findDistinctPatientsByDoctorAndDocumentNumber(doctor, documentNumber);
    }

    public void finishAppointment(Long appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        appointment.setStatus(Constants.AppointmentStatus.FINISHED);
        appointmentRepository.save(appointment);
    }

    public UserModel getPatientByAppointmentId(Long appointmentId) {
        return getAppointmentById(appointmentId).getPatient();
    }

    public List<AppointmentModel> getScheduledAppointmentsByPatient(UserModel patient) {
        return appointmentRepository.findByStatusAndPatient(Constants.AppointmentStatus.SCHEDULED, patient,
                Sort.by(Sort.Direction.ASC, "date"));
    }

    public List<AppointmentModel> getFinishedAppointmentsByPatient(UserModel patient) {
        return appointmentRepository.findByStatusAndPatient(Constants.AppointmentStatus.FINISHED, patient,
                Sort.by(Sort.Direction.ASC, "date"));
    }

    public List<AppointmentModel> getAvailableAppointmentsByType(Constants.MedicalSpecialty medicalSpecialty) {
        return appointmentRepository.findByDoctorMedicalSpecialtyAndStatus(medicalSpecialty, Constants.AppointmentStatus.AVAILABLE);
    }

    public boolean userHasAppointmentOfType(Constants.MedicalSpecialty medicalSpecialty) {
        UserModel user = userService.getSessionUser();
        return appointmentRepository.existsByPatientAndDoctorMedicalSpecialtyAndStatus(
                user, medicalSpecialty, Constants.AppointmentStatus.SCHEDULED);
    }

    public AppointmentModel getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public void scheduleAppointment(Long appointmentId) {
        UserModel user = userService.getSessionUser();
        AppointmentModel appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("ID de cita inválido"));
        if (appointment.getStatus() != Constants.AppointmentStatus.AVAILABLE) {
            throw new IllegalStateException("Solo se pueden agendar citas disponibles");
        }
        appointment.setPatient(user);
        appointment.setStatus(Constants.AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        appointment.setPatient(null);
        appointment.setOrder(null);
        appointment.setStatus(Constants.AppointmentStatus.AVAILABLE);
        appointmentRepository.save(appointment);
    }

    public boolean isDoctorLaboratoryOrSpecialist(Long appointmentId) {
        AppointmentModel appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("ID de cita inválido"));
        Constants.MedicalSpecialty specialty = appointment.getDoctor().getMedicalSpecialty();
        return specialty == Constants.MedicalSpecialty.LABORATORY || specialty == Constants.MedicalSpecialty.SPECIALIST;
    }

}