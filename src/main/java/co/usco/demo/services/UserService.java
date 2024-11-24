package co.usco.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserModel findByDocument(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber);
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
        return userRepository.findByDocumentNumber(documentNumber);
    }

}
