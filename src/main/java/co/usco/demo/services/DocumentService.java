package co.usco.demo.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import co.usco.demo.models.Constants;
import co.usco.demo.models.DocumentModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.DocumentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserService userService;

    // Subir un archivo
    public void uploadFile(MultipartFile file, Constants.DocumentType documentType, Long patientId) throws IOException {
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
    public Page<DocumentModel> getDocumentsForCurrentUser(int page, int size, UserModel user) {
        return documentRepository.findByPatient(user, 
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadDate")));
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
    public Page<DocumentModel> getDocumentsForCurrentUserByType(String documentType, int page, int size) {
        UserModel user = userService.getSessionUser();
        Constants.DocumentType type = Constants.DocumentType.valueOf(documentType);
        return documentRepository.findByPatientAndType(user, type, PageRequest.of(page, size));
    }

    // Agregar atributos de documentos para el usuario actual
    public void addDocumentAttributesForCurrentUser(Model model, int page, int pageSize, UserModel user) {
        Page<DocumentModel> documentPage = getDocumentsForCurrentUser(page, pageSize, user);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", documentPage.getTotalPages());
        model.addAttribute("documents", documentPage.getContent());
    }

    // Agregar atributos de documentos para los resultados filtrados
    public void addDocumentAttributesForFilteredResults(Model model, String documentType, int page, int pageSize) {
        Page<DocumentModel> documentPage;

        if (documentType == null || documentType.isEmpty()) {
            documentPage = getDocumentsForCurrentUser(page, pageSize, userService.getSessionUser());
        } else {
            documentPage = getDocumentsForCurrentUserByType(documentType, page, pageSize);
            model.addAttribute("documentType", documentType);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", documentPage.getTotalPages());
        model.addAttribute("documents", documentPage.getContent());
    }


}


