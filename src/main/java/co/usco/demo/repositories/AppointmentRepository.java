package co.usco.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.usco.demo.models.AppointmentModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.models.constants.AppointmentStatus;
import co.usco.demo.models.constants.MedicalSpecialty;

public interface AppointmentRepository extends JpaRepository<AppointmentModel, Long> {

    List<AppointmentModel> findByStatus(AppointmentStatus status);

    List<AppointmentModel> findByStatusAndPatient(AppointmentStatus status, UserModel patient);

    List<AppointmentModel> findByDoctorMedicalSpecialty(MedicalSpecialty medicalSpecialty);

    List<AppointmentModel> findByDoctorMedicalSpecialtyAndStatus(MedicalSpecialty medicalSpecialty, AppointmentStatus status);
}
