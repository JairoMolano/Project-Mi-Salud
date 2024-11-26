package co.usco.demo.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.usco.demo.models.AppointmentModel;
import co.usco.demo.models.Constants;
import co.usco.demo.models.UserModel;

public interface AppointmentRepository extends JpaRepository<AppointmentModel, Long> {

    List<AppointmentModel> findByStatus(Constants.AppointmentStatus status);

    List<AppointmentModel> findByStatusAndPatient(Constants.AppointmentStatus status, UserModel patient, Sort sort);

    List<AppointmentModel> findByDoctorMedicalSpecialty(Constants.MedicalSpecialty medicalSpecialty);

    List<AppointmentModel> findByDoctorMedicalSpecialtyAndStatus(Constants.MedicalSpecialty medicalSpecialty, Constants.AppointmentStatus status);

    boolean existsByPatientAndDoctorMedicalSpecialtyAndStatus(UserModel patient, Constants.MedicalSpecialty medicalSpecialty, Constants.AppointmentStatus status);

    boolean existsByDoctorAndDateAndTime(UserModel doctor, LocalDate date, LocalTime time);

    List<AppointmentModel> findByStatusAndDoctorAndDate(Constants.AppointmentStatus status, UserModel doctor, LocalDate today, Sort sort);

    List<AppointmentModel> findByStatusAndDoctor(Constants.AppointmentStatus status, UserModel doctor, Sort sort);

    @Query("SELECT DISTINCT a.patient FROM AppointmentModel a WHERE a.doctor = :doctor")
    List<UserModel> findDistinctPatientsByDoctor(@Param("doctor") UserModel doctor);

    List<AppointmentModel> findByStatusAndDoctorAndDateNot(Constants.AppointmentStatus status, UserModel doctor, LocalDate today, Sort sort);

    @Query("SELECT DISTINCT a.patient FROM AppointmentModel a WHERE a.doctor = :doctor AND a.patient.documentNumber = :documentNumber")
    List<UserModel> findDistinctPatientsByDoctorAndDocumentNumber(@Param("doctor") UserModel doctor, @Param("documentNumber") String documentNumber);

}
