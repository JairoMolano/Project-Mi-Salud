package co.usco.demo.services;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public void addUserAppointments(Model model, UserModel user) {
        model.addAttribute("scheduledAppointments", getScheduledAppointmentsByPatient(user));
        model.addAttribute("finishedAppointments", getFinisheddAppointmentsByPatient(user));
    }

    public String handleScheduleAppointment(Constants.MedicalSpecialty appointmentType, Model model) {
        model.addAttribute("path", "/patient/appointments");
        Constants.OrderType orderType = null;
        if (appointmentType == Constants.MedicalSpecialty.LABORATORY){
            orderType = Constants.OrderType.LAB_APPOINTMENT;
        } else if (appointmentType == Constants.MedicalSpecialty.SPECIALIST) {
            orderType = Constants.OrderType.SPECIALIST_APPOINTMENT;
        }
        if (appointmentType == Constants.MedicalSpecialty.LABORATORY || appointmentType == Constants.MedicalSpecialty.SPECIALIST) {
            List<OrderModel> authorizedOrders = orderService.getOrdersByTypeAndPatientAndAuthorized(orderType, userService.getSessionUser());
            if (authorizedOrders.isEmpty()) {
                model.addAttribute("errorMessage", getMessage("dontHaveOrderAuthorization"));
                return "patient/schedule-appointment/select-type";
            }
            model.addAttribute("appointmentType", appointmentType);
            model.addAttribute("authorizedOrders", authorizedOrders);
            return "patient/schedule-appointment/select-order";
        }
        return "patient/" + checkScheduledAppointments(appointmentType, model);
    }

    public String handleConfirmAppointmentOrder(Long orderId, Constants.MedicalSpecialty appointmentType, Model model, HttpSession session) {
        OrderModel order = orderService.getOrderById(orderId);
        model.addAttribute("appointments", getAvailableAppointmentsByType(appointmentType));
        session.setAttribute("orderId", order.getId());
        return "patient/schedule-appointment/select-appointment";
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

    public List<UserModel> getPatientsByDoctor() {
        UserModel doctor = userService.getSessionUser();
        return appointmentRepository.findDistinctPatientsByDoctor(doctor);
    }


    public List<UserModel> getPatientsByDoctorAndDocumentNumber(String documentNumber) {
        UserModel doctor = userService.getSessionUser();
        return appointmentRepository.findDistinctPatientsByDoctorAndDocumentNumber(doctor, documentNumber);
    }
        
    public List<AppointmentModel> getScheduledAppointmentsByDoctorExcludingToday() {
        UserModel doctor = userService.getSessionUser();
        LocalDate today = LocalDate.now();
        return appointmentRepository.findByStatusAndDoctorAndDateNot(Constants.AppointmentStatus.SCHEDULED, doctor, today, 
            Sort.by(Sort.Direction.ASC, "date"));
    }

    public UserModel getPatientByAppointmentId(Long appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        return appointment.getPatient();
    }

    public void finishAppointment(Long appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        appointment.setStatus(Constants.AppointmentStatus.FINISHED);
        appointmentRepository.save(appointment);
    }

    public List<AppointmentModel> getScheduledAppointmentsByPatient(UserModel patient) {
        return appointmentRepository.findByStatusAndPatient(Constants.AppointmentStatus.SCHEDULED, patient, 
                Sort.by(Sort.Direction.ASC, "date"));
    }
    
    public List<AppointmentModel> getFinisheddAppointmentsByPatient(UserModel patient) {
        return appointmentRepository.findByStatusAndPatient(Constants.AppointmentStatus.FINISHED, patient, 
                Sort.by(Sort.Direction.ASC, "date"));
    }

    public AppointmentModel getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<AppointmentModel> getAvailableAppointmentsByType(Constants.MedicalSpecialty medicalSpecialty) {
        return appointmentRepository.findByDoctorMedicalSpecialtyAndStatus(medicalSpecialty, Constants.AppointmentStatus.AVAILABLE);
    }

    public String checkScheduledAppointments(Constants.MedicalSpecialty appointmentType, Model model) {
        boolean hasAppointment = userHasAppointmentOfType(appointmentType);
        if (hasAppointment) {
            model.addAttribute("errorMessage", getMessage("alreadyAppointmentScheduled"));
            return "schedule-appointment/select-type";
        } else {
            model.addAttribute("appointments", getAvailableAppointmentsByType(appointmentType));
            return "schedule-appointment/select-appointment";
        }
    }

    public boolean userHasAppointmentOfType(Constants.MedicalSpecialty medicalSpecialty) {
        UserModel user = userService.getSessionUser(); 
        return appointmentRepository.existsByPatientAndDoctorMedicalSpecialtyAndStatus(
                user, medicalSpecialty, Constants.AppointmentStatus.SCHEDULED
        );
    }

    public void scheduleAppointment(Long appointmentId) {
        UserModel user = userService.getSessionUser();
        AppointmentModel appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));
            if (appointment.getStatus() == Constants.AppointmentStatus.AVAILABLE) {
                appointment.setPatient(user);
                appointment.setStatus(Constants.AppointmentStatus.SCHEDULED);
                appointmentRepository.save(appointment);
            } else {
                throw new IllegalStateException("Only available appointments can be scheduled");
            }
    }

    public boolean isDoctorLaboratoryOrSpecialist(Long appointmentId) {
        AppointmentModel appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));
        Constants.MedicalSpecialty medicalSpecialty = appointment.getDoctor().getMedicalSpecialty();
        if (medicalSpecialty == Constants.MedicalSpecialty.LABORATORY || medicalSpecialty == Constants.MedicalSpecialty.SPECIALIST) {
            return true;
        } else {
            return false;
        }
    }

    public void cancelAppointment(Long appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        appointment.setPatient(null);
        appointment.setOrder(null);
        appointment.setStatus(Constants.AppointmentStatus.AVAILABLE);
        appointmentRepository.save(appointment);
    }



}
