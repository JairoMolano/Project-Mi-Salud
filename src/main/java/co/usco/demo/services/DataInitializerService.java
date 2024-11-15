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

        // ROLES
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

        // USUARIOS
        UserModel userPatient1 = UserModel.builder()
            .documentNumber("1234")
            .documentType("cc")
            .username("Patient1")
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(rolePatient))
            .build();

        UserModel userPatient2 = UserModel.builder()
            .documentNumber("4321")
            .documentType("cc")
            .username("Patient2")
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(rolePatient))
            .build();

        UserModel userSupportStaff1 = UserModel.builder()
            .documentNumber("5678")
            .documentType("cc")
            .username("SupportStaff1")
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(roleSupportStaff))
            .build();

        UserModel userSupportStaff2 = UserModel.builder()
            .documentNumber("8765")
            .documentType("cc")
            .username("SupportStaff2")
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(roleSupportStaff))
            .build();

        UserModel userMedicalStaff1 = UserModel.builder()
            .documentNumber("9090")
            .documentType("cc")
            .username("MedicalStaff1")
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(roleMedicalStaff))
            .build();

        UserModel userMedicalStaff2 = UserModel.builder()
            .documentNumber("0909")
            .documentType("cc")
            .username("MedicalStaff2")
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(roleMedicalStaff))
            .build();

        UserModel userAdmin = UserModel.builder()
            .documentNumber("0000")
            .documentType("cc")
            .username("Admin")
            .password(passwordEncoder.encode("1234"))
            .roles(Set.of(roleAdmin))
            .build();

        userRepository.saveAll(List.of(userPatient1, userPatient2, userSupportStaff1, userSupportStaff2, userMedicalStaff1, userMedicalStaff2, userAdmin));

    }
}
