package co.usco.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.usco.demo.models.AppointmentModel;
import co.usco.demo.models.DocumentModel;
import co.usco.demo.models.RoleModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.AppointmentRepository;
import co.usco.demo.repositories.DocumentRepository;
import co.usco.demo.repositories.RoleRepository;
import co.usco.demo.repositories.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import co.usco.demo.models.OrderModel;
import co.usco.demo.repositories.OrderRepository;
import co.usco.demo.models.Constants;

@Service
public class DataInitializerService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderRepository orderRepository;


    @Transactional
    public void initializeData() {

        // Creation of roles
        RoleModel rolePatient = RoleModel.builder()
            .roleName("ROLE_PATIENT")
            .permissions(Set.of(Constants.Permission.READ, Constants.Permission.UPDATE, Constants.Permission.CREATE))
            .build();

        RoleModel roleSupportStaff = RoleModel.builder()
            .roleName("ROLE_SUPPORT_STAFF")
            .permissions(Set.of(Constants.Permission.READ, Constants.Permission.UPDATE, Constants.Permission.CREATE, Constants.Permission.DELETE))
            .build();

        RoleModel roleMedicalStaff = RoleModel.builder()
            .roleName("ROLE_MEDICAL_STAFF")
            .permissions(Set.of(Constants.Permission.READ, Constants.Permission.UPDATE, Constants.Permission.CREATE, Constants.Permission.DELETE))
            .build();

        RoleModel roleAdmin = RoleModel.builder()
            .roleName("ROLE_ADMIN")
            .permissions(Set.of(Constants.Permission.READ, Constants.Permission.UPDATE, Constants.Permission.CREATE, Constants.Permission.DELETE))
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
            .roles(Set.of(rolePatient))
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
            .roles(Set.of(rolePatient))
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
            .roles(Set.of(roleAdmin))
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
            .roles(Set.of(roleMedicalStaff))
            .medicalSpecialty(Constants.MedicalSpecialty.GENERAL)
            .horaryStart(LocalTime.of(6, 0))
            .horaryEnd(LocalTime.of(18, 0))
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
            .userActive(true)
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(roleMedicalStaff))
            .medicalSpecialty(Constants.MedicalSpecialty.DENTISTRY)
            .horaryStart(LocalTime.of(6, 0))
            .horaryEnd(LocalTime.of(18, 0))
            .build();

        UserModel user8 = UserModel.builder()
            .firstName("Pedro")
            .lastName("Gonzalez")
            .documentType("cc")
            .documentNumber("7890123456")
            .gender("Male")
            .phoneNumber("3607890123")
            .email("pedro.gonzalez@example.com")
            .address("Calle 80 # 90-100")
            .department("Boyaca")
            .city("Tunja")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(roleMedicalStaff))
            .medicalSpecialty(Constants.MedicalSpecialty.LABORATORY)
            .horaryStart(LocalTime.of(6, 0))
            .horaryEnd(LocalTime.of(18, 0))
            .build();

        UserModel user9 = UserModel.builder()
            .firstName("Lucia")
            .lastName("Martinez")
            .documentType("cc")
            .documentNumber("8901234567")
            .gender("Female")
            .phoneNumber("3708901234")
            .email("lucia.martinez@example.com")
            .address("Carrera 50 # 60-70")
            .department("Caldas")
            .city("Manizales")
            .password(passwordEncoder.encode("1234"))
            .userActive(true)
            .roles(Set.of(roleMedicalStaff))
            .medicalSpecialty(Constants.MedicalSpecialty.SPECIALIST)
            .horaryStart(LocalTime.of(6, 0))
            .horaryEnd(LocalTime.of(18, 0))
            .build();

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9));

        // Creation of documents
        DocumentModel document1 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(Constants.DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        DocumentModel document2 = DocumentModel.builder()
            .name("RESULTADO DE LABORATORIO")
            .type(Constants.DocumentType.LABORATORY_RESULT)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168096134-RESULTADO DE LABORATORIO.pdf")
            .build();

        DocumentModel document3 = DocumentModel.builder()
            .name("RADIOGRAFIA")
            .type(Constants.DocumentType.DIAGNOSTIC_IMAGE)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168105946-RADIOGRAFIA.pdf")
            .build();

        DocumentModel document4 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(Constants.DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        DocumentModel document5 = DocumentModel.builder()
            .name("RESULTADO DE LABORATORIO")
            .type(Constants.DocumentType.LABORATORY_RESULT)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168096134-RESULTADO DE LABORATORIO.pdf")
            .build();

        DocumentModel document6 = DocumentModel.builder()
            .name("RADIOGRAFIA")
            .type(Constants.DocumentType.DIAGNOSTIC_IMAGE)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168105946-RADIOGRAFIA.pdf")
            .build();

        DocumentModel document7 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(Constants.DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        DocumentModel document8 = DocumentModel.builder()
            .name("RESULTADO DE LABORATORIO")
            .type(Constants.DocumentType.LABORATORY_RESULT)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168096134-RESULTADO DE LABORATORIO.pdf")
            .build();

        DocumentModel document9 = DocumentModel.builder()
            .name("RADIOGRAFIA")
            .type(Constants.DocumentType.DIAGNOSTIC_IMAGE)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user2)
            .path("documents/1732168105946-RADIOGRAFIA.pdf")
            .build();

        DocumentModel document10 = DocumentModel.builder()
            .name("RESULTADO DE ESPECIALISTA")
            .type(Constants.DocumentType.OTHER)
            .uploadDate(java.sql.Date.valueOf(LocalDate.now()))
            .patient(user1)
            .uploadBy(user3)
            .path("documents/1732168088035-RESULTADO DE ESPECIALISTA.pdf")
            .build();

        documentRepository.saveAll(List.of(document1, document2, document3, document4, document5, document6, document7, document8, document9, document10));

        

        OrderModel order1 = OrderModel.builder()
            .orderType(Constants.OrderType.MEDICATION)
            .status(Constants.OrderStatus.AUTHORIZED)
            .description("MOTIVO DE ORDEN: Paciente con diagnóstico de diabetes mellitus tipo 2. Presenta cifras de glucemia elevadas a pesar de tratamiento actual. Se ajusta esquema terapéutico. SE SOLICITA: Agregar metformina 500 mg 1 comprimido por vía oral 2 veces al día.")
            .patient(user1)
            .doctor(user6)
            .createdAt(LocalDateTime.now())
            .authorizedBy(user5)
            .authorizedAt(LocalDateTime.now())
            .build();

        OrderModel order2 = OrderModel.builder()
            .orderType(Constants.OrderType.LAB_APPOINTMENT)
            .status(Constants.OrderStatus.AUTHORIZED)
            .description("MOTIVO DE ORDEN: Paciente con dolor abdominal en cuadrante superior derecho de 24 horas de evolución. Sospecha de colecistitis aguda. SE SOLICITA: Hemograma completo. Perfil bioquímico (incluyendo bilirrubina total y directa, transaminasas, fosfatasa alcalina, gamma-glutamil transferasa).")
            .authorizedBy(user5)
            .authorizedAt(LocalDateTime.now())
            .patient(user1)
            .doctor(user6)
            .createdAt(LocalDateTime.now())
            .build();

        OrderModel order3 = OrderModel.builder()
            .orderType(Constants.OrderType.SPECIALIST_APPOINTMENT)
            .status(Constants.OrderStatus.REQUESTING)
            .description("MOTIVO DE ORDEN: Paciente con hipertensión arterial resistente a tratamiento. Se recomienda valoración por especialista en Nefrología para descartar enfermedad renal crónica y ajustar tratamiento antihipertensivo. SE SOLICITA: Agendar cita con especialista en un plazo no mayor a 15 días.")
            .patient(user1)
            .doctor(user6)
            .createdAt(LocalDateTime.now())
            .build();

        OrderModel order4 = OrderModel.builder()
            .orderType(Constants.OrderType.LAB_APPOINTMENT)
            .status(Constants.OrderStatus.PENDING)
            .description("MOTIVO DE ORDEN: Paciente con hernia inguinal derecha. Se indica reparación quirúrgica por laparoscopía. SE SOLICITA: Realizar exámenes preoperatorios (hemograma, coagulación, grupo sanguíneo y factor Rh).")
            .patient(user1)
            .doctor(user6)
            .createdAt(LocalDateTime.now())
            .build();

        OrderModel order5 = OrderModel.builder()
            .orderType(Constants.OrderType.LAB_APPOINTMENT)
            .status(Constants.OrderStatus.COMPLETED)
            .description("MOTIVO DE ORDEN: Paciente con cefaleas recurrentes y vómitos. Sospecha de hipertensión intracraneal. SE SOLICITA: Resonancia magnética cerebral sin contraste.")
            .authorizedBy(user5)
            .authorizedAt(LocalDateTime.now())
            .patient(user1)
            .doctor(user6)
            .createdAt(LocalDateTime.now())
            .build();

        OrderModel order6= OrderModel.builder()
            .orderType(Constants.OrderType.MEDICATION)
            .status(Constants.OrderStatus.COMPLETED)
            .description("MOTIVO DE ORDEN: Paciente con diagnóstico de osteoartritis de rodilla. Presenta dolor crónico y limitación funcional. SE SOLICITA: Paracetamol 500 mg cada 8 horas por vía oral como primera línea analgésica. Ibuprofeno 600 mg cada 8 horas por vía oral si el dolor persiste.")
            .authorizedBy(user5)
            .authorizedAt(LocalDateTime.now())
            .patient(user1)
            .doctor(user6)
            .createdAt(LocalDateTime.now())
            .build();

        orderRepository.saveAll(List.of(order1, order2, order3, order4, order5, order6));

        AppointmentModel appointment1 = AppointmentModel.builder()
            .date(LocalDate.now())
            .time(LocalTime.of(8, 0))
            .place(Constants.AppointmentPlace.CLINIC_CENTER)
            .status(Constants.AppointmentStatus.SCHEDULED)
            .doctor(user6)
            .patient(user1)
            .build();

        AppointmentModel appointment2 = AppointmentModel.builder()
            .date(LocalDate.now())
            .time(LocalTime.of(9, 0))
            .place(Constants.AppointmentPlace.CLINIC_CENTER)
            .status(Constants.AppointmentStatus.SCHEDULED)
            .doctor(user7)
            .patient(user1)
            .build();

        AppointmentModel appointment3 = AppointmentModel.builder()
            .date(LocalDate.of(2024, 11, 25))
            .time(LocalTime.of(9, 0))
            .place(Constants.AppointmentPlace.CLINIC_CENTER)
            .status(Constants.AppointmentStatus.FINISHED)
            .doctor(user6)
            .patient(user1)
            .build();

        appointmentRepository.saveAll(List.of(appointment1, appointment2, appointment3));

        
        generateAppointmentsForMedicalStaff();

    }



    private void generateAppointmentsForMedicalStaff() {
        List<UserModel> medicalStaff = userRepository.findByRolesRoleName("ROLE_MEDICAL_STAFF");
        for (UserModel doctor : medicalStaff) {
            LocalTime startTime = doctor.getHoraryStart();
            LocalTime endTime = doctor.getHoraryEnd();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusWeeks(1);

            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    LocalTime currentTime = startTime;
                    while (currentTime.isBefore(endTime)) {
                        // Omitir el intervalo de 12pm a 2pm
                        if (currentTime.isBefore(LocalTime.of(12, 0)) || currentTime.isAfter(LocalTime.of(13, 40))) {
                            if (!appointmentRepository.existsByDoctorAndDateAndTime(doctor, date, currentTime)) {
                                AppointmentModel appointment = AppointmentModel.builder()
                                    .date(date)
                                    .time(currentTime)
                                    .place(doctor.getMedicalSpecialty() == Constants.MedicalSpecialty.LABORATORY ? Constants.AppointmentPlace.DIAGNOSTIC_CENTER :
                                           doctor.getMedicalSpecialty() == Constants.MedicalSpecialty.SPECIALIST ? Constants.AppointmentPlace.CLINIC_CENTER :
                                           doctor.getMedicalSpecialty() == Constants.MedicalSpecialty.DENTISTRY ? Constants.AppointmentPlace.DENTAL_CENTER :
                                           Constants.AppointmentPlace.CLINIC_CENTER)
                                    .status(Constants.AppointmentStatus.AVAILABLE)
                                    .doctor(doctor)
                                    .build();
                                appointmentRepository.save(appointment);
                            }
                        }
                        currentTime = currentTime.plusMinutes(20);
                    }
                }
            }
        }
    }
    

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailyAppointments() {
        generateAppointmentsForMedicalStaff();
    }

}
