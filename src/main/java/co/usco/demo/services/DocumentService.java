package co.usco.demo.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import co.usco.demo.models.DocumentModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.models.constants.DocumentType;
import co.usco.demo.repositories.DocumentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserService userService;

    // Subir un archivo
    public void uploadFile(MultipartFile file, DocumentType documentType, Long patientId) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "-" + fileName;
            Path filePath = Paths.get("documents/" + uniqueFileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            UserModel user = userService.getSessionUser(); // El usuario que sube el documento
            UserModel patient = userService.getUserById(patientId); // Obtener al paciente

            DocumentModel document = new DocumentModel();
            document.setName(fileName);
            document.setPath(filePath.toString());
            document.setType(documentType);  // Asignar el tipo de documento
            document.setUploadBy(user); // Usuario que sube el documento
            document.setPatient(patient); // Paciente asociado

            documentRepository.save(document);
        }
    }

    // Listar documentos de un usuario
    public List<DocumentModel> getDocumentsForCurrentUser() {
        UserModel user = userService.getSessionUser();
        return documentRepository.findByPatient(user); // Asumiendo que quieres ver los documentos subidos por el usuario
    }

    // Obtener documento por ID
    public Optional<DocumentModel> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    // Descargar documento
    public void downloadDocument(Long id, HttpServletResponse response) throws IOException {
        DocumentModel document = getDocumentById(id).orElseThrow(() -> new FileNotFoundException("Documento no encontrado"));
        Path filePath = Paths.get(document.getPath());
        File file = filePath.toFile();

        if (file.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"" + document.getName() + "\"");

            try (InputStream inputStream = new FileInputStream(file)) {
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
            }
        } else {
            throw new FileNotFoundException("Archivo no encontrado");
        }
    }

    // Listar documentos de un usuario por tipo
    public List<DocumentModel> getDocumentsForCurrentUserByType(DocumentType documentType) {
        UserModel user = userService.getSessionUser();
        return documentRepository.findByPatientAndType(user, documentType);
    }
}


