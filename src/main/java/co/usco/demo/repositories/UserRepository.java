package co.usco.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.usco.demo.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{

    Optional<UserModel> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);

    UserModel findByEmail(String email);
    
    boolean existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);

    Optional<UserModel> findByDocumentNumberAndVerificationCode(String documentNumber, String verificationCode);

    UserModel findUserByDocumentNumber(String documentNumber);

    List<UserModel> findByRolesRoleName(String roleName);

    List<UserModel> findByRolesRoleNameAndDocumentNumber(String roleName, String documentNumber);

    Optional<UserModel> findByDocumentNumber(String documentNumber);
}