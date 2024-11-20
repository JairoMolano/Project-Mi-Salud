package co.usco.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.usco.demo.models.DocumentModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.models.constants.DocumentType;

public interface DocumentRepository extends JpaRepository<DocumentModel, Long> {
    List<DocumentModel> findByPatient(UserModel user);

    List<DocumentModel> findByPatientAndType(UserModel user, DocumentType type);
    
}
