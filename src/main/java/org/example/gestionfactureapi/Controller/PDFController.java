package org.example.gestionfactureapi.Controller;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.File;
import org.example.gestionfactureapi.Repository.FileRepository;
import org.example.gestionfactureapi.Service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class PDFController {

    private final FileService fileService;
    private final FileRepository fileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/generate-pdf")
    public ResponseEntity<String> generatePDF(@RequestBody BonCmdA bonCmdA) {
        try {
            fileService.createAndSavePDF(bonCmdA);
            return ResponseEntity.ok("PDF generated and saved successfully.");
        } catch (DocumentException | IOException | URISyntaxException e) {
            return ResponseEntity.status(500).body("Error generating PDF: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable String filename) {
        Optional<File> fileOptional = fileRepository.findByFileName(filename);

        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(file.getPdfData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}