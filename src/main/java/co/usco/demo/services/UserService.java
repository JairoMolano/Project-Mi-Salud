package co.usco.demo.services;

import org.aspectj.apache.bcel.classfile.Unknown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.usco.demo.models.Constants;
import co.usco.demo.models.RoleModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.RoleRepository;
import co.usco.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;

import java.lang.annotation.Native;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    public UserModel findByDocument(String documentNumber) {
        return userRepository.findUserByDocumentNumber(documentNumber);
    }

    public UserModel getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserModel findByDocumentNumberAndDocumentType(String documentNumber, String documentType) {
        return userRepository.findByDocumentNumberAndDocumentType(documentNumber, documentType).orElse(null);
    }

    public boolean documentExists(String documentType, String documentNumber) {
        return userRepository.existsByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }

    public void save(UserModel user) {
        userRepository.save(user);
    }

    public UserModel getSessionUser() {
        String documentNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByDocumentNumber(documentNumber);
    }

    public List<UserModel> getAllUsersByRol(String role) {
        return userRepository.findByRolesRoleName(role);
    }

    public List<UserModel> getUsersByRolAndDocumentNumber(String role, String documentNumber) {
        return userRepository.findByRolesRoleNameAndDocumentNumber(role, documentNumber);
    }



    @Transactional
    public void registerStaff(UserModel user, String roleName) {
        RoleModel role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + roleName));
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    @Transactional
    public void assignRole(Long userId, String roleName, String medicalSpecialty) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        RoleModel role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + roleName));
        
        // Crear una nueva colección mutable para los roles
        Set<RoleModel> roles = new HashSet<>(user.getRoles());
        roles.clear(); // Limpiar la colección actual
        roles.add(role); // Agregar el nuevo rol
        user.setRoles(roles);

        if (roleName.equals("ROLE_MEDICAL_STAFF")) {
            user.setMedicalSpecialty(Constants.MedicalSpecialty.valueOf(medicalSpecialty));
        }
        userRepository.save(user);
    }


    @Transactional
    public void updateUser(UserModel updatedUser) {
        UserModel existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + updatedUser.getId()));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setCity(updatedUser.getCity());
        
        userRepository.save(existingUser);
    }


    public Optional<UserModel> findByDocumentNumber(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber);
    }
    
}
