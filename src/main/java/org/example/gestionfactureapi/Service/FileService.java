package org.example.gestionfactureapi.Service;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.File;
import org.example.gestionfactureapi.Repository.FileRepository;
import org.example.gestionfactureapi.pdf.PDFGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final PDFGeneration pdfGeneration;

    public void createAndSavePDF(BonCmdA bonCmdA) throws DocumentException, IOException, URISyntaxException {
        // Generate PDF
        PDFGeneration pdfGeneration = new PDFGeneration(bonCmdA);
        byte[] pdfData = pdfGeneration.run();

        // Create File entity
        File file = new File(pdfData, "invoice"+bonCmdA.getId()+".pdf", "application/pdf");

        // Save File entity to the database
        fileRepository.save(file);
    }
    public File findByFileName(String fileName) {
        return fileRepository.findByFileName(fileName).orElseThrow(
                ()->new EntityNotFoundException("File Not Found")
        );
    }
}