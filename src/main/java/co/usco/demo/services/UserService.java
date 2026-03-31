package co.usco.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import co.usco.demo.models.Constants;
import co.usco.demo.models.RoleModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.RoleRepository;
import co.usco.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
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

    public Optional<UserModel> findByDocumentNumber(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber);
    }

    public void save(UserModel user) {
        userRepository.save(user);
    }

    @Transactional
    public void registerStaff(UserModel user, String roleName, String medicalSpecialty, String horaryStart, String horaryEnd) {
        RoleModel role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Rol inválido: " + roleName));

        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if ("ROLE_MEDICAL_STAFF".equals(roleName) && medicalSpecialty != null) {
            user.setMedicalSpecialty(Constants.MedicalSpecialty.valueOf(medicalSpecialty));
            if (horaryStart != null && !horaryStart.isEmpty()) {
                user.setHoraryStart(LocalTime.parse(horaryStart));
            }
            if (horaryEnd != null && !horaryEnd.isEmpty()) {
                user.setHoraryEnd(LocalTime.parse(horaryEnd));
            }
        }

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserModel updatedUser) {
        UserModel existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido: " + updatedUser.getId()));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setCity(updatedUser.getCity());
        userRepository.save(existingUser);
    }

    @Transactional
    public void updateProfilePicture(UserModel user, MultipartFile file) throws IOException {
        String oldPath = user.getProfilePicturePath();
        if (oldPath != null && !oldPath.equals("/profile-pictures/profile-picture-default.png")) {
            String oldFileName = oldPath.substring(oldPath.lastIndexOf("/") + 1);
            Path oldFilePath = Paths.get("profile-pictures/" + oldFileName);
            Files.deleteIfExists(oldFilePath);
        }

        String newFileName = file.getOriginalFilename();
        Path newFilePath = Paths.get("profile-pictures/" + newFileName);
        Files.write(newFilePath, file.getBytes());

        user.setProfilePicturePath("/profile-pictures/" + newFileName);
        userRepository.save(user);
    }

    @Transactional
    public void assignRole(Long userId, String roleName, String medicalSpecialty) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido: " + userId));
        RoleModel role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Rol inválido: " + roleName));

        Set<RoleModel> roles = new HashSet<>(user.getRoles());
        roles.clear();
        roles.add(role);
        user.setRoles(roles);

        if ("ROLE_MEDICAL_STAFF".equals(roleName) && medicalSpecialty != null) {
            user.setMedicalSpecialty(Constants.MedicalSpecialty.valueOf(medicalSpecialty));
        }

        userRepository.save(user);
    }

}