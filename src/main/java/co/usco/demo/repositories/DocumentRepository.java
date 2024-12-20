package co.usco.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.usco.demo.models.Constants;
import co.usco.demo.models.DocumentModel;
import co.usco.demo.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentRepository extends JpaRepository<DocumentModel, Long> {

    Page<DocumentModel> findByPatientAndType(UserModel user, Constants.DocumentType type, Pageable pageable);

    Page<DocumentModel> findByPatient(UserModel user, Pageable pageable);
    
}

