package co.usco.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.usco.demo.models.constants.Permission;
import co.usco.demo.models.RoleModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.RoleRepository;
import co.usco.demo.repositories.UserRepository;
import java.util.List;
import java.util.Set;

@Service
public class DataInitializerService {

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserRepository userRepository;

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
            .roles(Set.of(roleAdmin))
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
            .email("laura.fernandez@example.com")
            .address("Avenida 40 # 50-60")
            .department("Bolivar")
            .city("Cartagena")
            .userActive(false)
            .roles(Set.of(rolePatient))
            .build();

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7));

    }
}
