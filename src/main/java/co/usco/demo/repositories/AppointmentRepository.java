package co.usco.demo.repositories;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import co.usco.demo.models.AppointmentModel;
import co.usco.demo.models.Constants;
import co.usco.demo.models.UserModel;

public interface AppointmentRepository extends JpaRepository<AppointmentModel, Long> {

    List<AppointmentModel> findByStatus(Constants.AppointmentStatus status);

    List<AppointmentModel> findByStatusAndPatient(Constants.AppointmentStatus status, UserModel patient, Sort sort);

    List<AppointmentModel> findByDoctorMedicalSpecialty(Constants.MedicalSpecialty medicalSpecialty);

    List<AppointmentModel> findByDoctorMedicalSpecialtyAndStatus(Constants.MedicalSpecialty medicalSpecialty, Constants.AppointmentStatus status);

    boolean existsByPatientAndDoctorMedicalSpecialtyAndStatus(UserModel patient, Constants.MedicalSpecialty medicalSpecialty, Constants.AppointmentStatus status);
    
}
