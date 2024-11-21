package co.usco.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.usco.demo.models.constants.AppointmentStatus;
import co.usco.demo.models.constants.DocumentType;
import co.usco.demo.models.constants.MedicalSpecialty;
import co.usco.demo.models.constants.Permission;
import co.usco.demo.models.AppointmentModel;
import co.usco.demo.models.DocumentModel;
import co.usco.demo.models.RoleModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.AppointmentRepository;
import co.usco.demo.repositories.DocumentRepository;
import co.usco.demo.repositories.RoleRepository;
import co.usco.demo.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class DataInitializerService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentsRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void initializeData() {

        // Creation of roles
        RoleModel rolePatient = RoleModel.builder()
            .roleName("ROLE_PATIENT")
            .permissions(Set.of(Permission.READ, Permission.UPDATE, Permission.CREATE))
            .build();

        RoleModel roleSupportStaff = RoleModel.builder()
            .roleName("ROLE_SUPPORT_STAFF")
            .permissions(Set.of(Permission.READ, Permission.UPDATE, Permission.CREATE, Permission.DELETE))
            .build();

        RoleModel roleMedicalStaff = RoleModel.builder()
            .roleName("ROLE_MEDICAL_STAFF")
            .permissions(Set.of(Permission.READ, Permission.UPDATE, Permission.CREATE, Permission.DELETE))
            .build();

        RoleModel roleAdmin = RoleModel.builder()
            .roleName("ROLE_ADMIN")
            .permissions(Set.of(Permission.READ, Permission.UPDATE, Permission.CREATE, Permission.DELETE))
            .build();

        roleRepository.saveAll(List.of(rolePatient, roleSupportStaff, roleMedicalStaff, roleAdmin));

        // Creation of users
        UserModel user1 = UserModel.builder()
            .firstName("Jairo")
            .lastName("Molano")
            .documentType("cc")
            .documentNumber("0000")
            .gender("Male")
            .phoneNumber("3001234050")
            .email("jairo.molano@example.com")
            .address("Calle 1 # 2-3")
            .department("Huila")
            .city("Neiva")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(rolePatient))
            .build();

        UserModel user2 = UserModel.builder()
            .firstName("Carlos")
            .lastName("Perez")
            .documentType("cc")
            .documentNumber("1234567890")
            .gender("Male")
            .phoneNumber("3001234567")
            .email("carlos.perez@example.com")
            .address("Carrera 10 # 20-30")
            .department("Antioquia")
            .city("Medellin")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(roleMedicalStaff))
            .medicalSpecialty(MedicalSpecialty.GENERAL)
            .build();

        UserModel user3 = UserModel.builder()
            .firstName("Ana")
            .lastName("Gomez")
            .documentType("cc")
            .documentNumber("2345678901")
            .gender("Female")
            .phoneNumber("3102345678")
            .email("ana.gomez@example.com")
            .address("Calle 50 # 60-70")
            .department("Cundinamarca")
            .city("Bogota")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(roleMedicalStaff))
            .medicalSpecialty(MedicalSpecialty.DENTISTRY)
            .build();

        UserModel user4 = UserModel.builder()
            .firstName("Luis")
            .lastName("Martinez")
            .documentType("cc")
            .documentNumber("3456789012")
            .gender("Male")
            .phoneNumber("3203456789")
            .email("luis.martinez@example.com")
            .address("Avenida 30 # 40-50")
            .department("Valle del Cauca")
            .city("Cali")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(roleSupportStaff))
            .build();

        UserModel user5 = UserModel.builder()
            .firstName("Maria")
            .lastName("Lopez")
            .documentType("cc")
            .documentNumber("4567890123")
            .gender("Female")
            .phoneNumber("3304567890")
            .email("maria.lopez@example.com")
            .address("Calle 70 # 80-90")
            .department("Santander")
            .city("Bucaramanga")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(roleSupportStaff))
            .build();

        UserModel user6 = UserModel.builder()
            .firstName("Jorge")
            .lastName("Ramirez")
            .documentType("cc")
            .documentNumber("5678901234")
            .gender("Male")
            .phoneNumber("3405678901")
            .email("jorge.ramirez@example.com")
            .address("Carrera 20 # 30-40")
            .department("Atlantico")
            .city("Barranquilla")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(rolePatient))
            .build();

        UserModel user7 = UserModel.builder()
            .firstName("Laura")
            .lastName("Fernandez")
            .documentType("ti")
            .documentNumber("6789012345")
            .gender("Female")
            .phoneNumber("3506789012")
            .email("jmolanosalgado@gmail.com")
            .address("Avenida 40 # 50-60")
            .department("Bolivar")
            .city("Cartagena")
            .userActive(false)
            .roles(Set.of(rolePatient))
            .build();

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7));

        // Creation of appointments
        AppointmentModel appointment1 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 9, 30))
            .time(LocalTime.of(9, 0))
            .status(AppointmentStatus.SCHEDULED)
            .patient(user1)
            .doctor(user2)
            .build();

        AppointmentModel appointment2 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 1))
            .time(LocalTime.of(10, 0))
            .status(AppointmentStatus.SCHEDULED)
            .patient(user1)
            .doctor(user2)
            .build();

        AppointmentModel appointment3 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 2))
            .time(LocalTime.of(11, 0))
            .status(AppointmentStatus.FINISHED)
            .patient(user1)
            .doctor(user3)
            .build();

        AppointmentModel appointment4 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 3))
            .time(LocalTime.of(12, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment5 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 4))
            .time(LocalTime.of(13, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment6 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 5))
            .time(LocalTime.of(14, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment7 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 6))
            .time(LocalTime.of(15, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment8 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 7))
            .time(LocalTime.of(16, 0))
            .status(AppointmentStatus.SCHEDULED)
            .doctor(user3)
            .build();

        AppointmentModel appointment9 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 8))
            .time(LocalTime.of(17, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment10 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 9))
            .time(LocalTime.of(18, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment11 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 10))
            .time(LocalTime.of(9, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment12 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 11))
            .time(LocalTime.of(10, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment13 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 12))
            .time(LocalTime.of(11, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment14 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 13))
            .time(LocalTime.of(12, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment15 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 14))
            .time(LocalTime.of(13, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment16 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 15))
            .time(LocalTime.of(14, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment17 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 16))
            .time(LocalTime.of(15, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment18 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 17))
            .time(LocalTime.of(16, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment19 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 18))
            .time(LocalTime.of(17, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment20 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 19))
            .time(LocalTime.of(18, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment21 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 20))
            .time(LocalTime.of(9, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment22 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 21))
            .time(LocalTime.of(10, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment23 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 22))
            .time(LocalTime.of(11, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment24 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 23))
            .time(LocalTime.of(12, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment25 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 24))
            .time(LocalTime.of(13, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment26 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 25))
            .time(LocalTime.of(14, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment27 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 26))
            .time(LocalTime.of(15, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment28 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 27))
            .time(LocalTime.of(16, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        AppointmentModel appointment29 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 28))
            .time(LocalTime.of(17, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user2)
            .build();

        AppointmentModel appointment30 = AppointmentModel.builder()
            .date(LocalDate.of(2023, 10, 29))
            .time(LocalTime.of(18, 0))
            .status(AppointmentStatus.AVAILABLE)
            .doctor(user3)
            .build();

        appointmentsRepository.saveAll(List.of(
            appointment1, appointment2, appointment3, appointment4, appointment5, appointment6, appointment7, appointment8, appointment9, appointment10,
            appointment11, appointment12, appointment13, appointment14, appointment15, appointment16, appointment17, appointment18, appointment19, appointment20,
            appointment21, appointment22, appointment23, appointment24, appointment25, appointment26, appointment27, appointment28, appointment29, appointment30
        ));


        // Creation of documents
        DocumentModel document1 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        DocumentModel document2 = DocumentModel.builder()
            .name("RESULTADO DE LABORATORIO")
            .type(DocumentType.LABORATORY_RESULT)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168096134-RESULTADO DE LABORATORIO.pdf")
            .build();

        DocumentModel document3 = DocumentModel.builder()
            .name("RADIOGRAFIA")
            .type(DocumentType.RADIOGRAPHY)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168105946-RADIOGRAFIA.pdf")
            .build();

        DocumentModel document4 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        DocumentModel document5 = DocumentModel.builder()
            .name("RESULTADO DE LABORATORIO")
            .type(DocumentType.LABORATORY_RESULT)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168096134-RESULTADO DE LABORATORIO.pdf")
            .build();

        DocumentModel document6 = DocumentModel.builder()
            .name("RADIOGRAFIA")
            .type(DocumentType.RADIOGRAPHY)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168105946-RADIOGRAFIA.pdf")
            .build();

        DocumentModel document7 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        DocumentModel document8 = DocumentModel.builder()
            .name("RESULTADO DE LABORATORIO")
            .type(DocumentType.LABORATORY_RESULT)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168096134-RESULTADO DE LABORATORIO.pdf")
            .build();

        DocumentModel document9 = DocumentModel.builder()
            .name("RADIOGRAFIA")
            .type(DocumentType.RADIOGRAPHY)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168105946-RADIOGRAFIA.pdf")
            .build();

        DocumentModel document10 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        documentRepository.saveAll(List.of(document1, document2, document3, document4, document5, document6, document7, document8, document9, document10));

        

    }
}
