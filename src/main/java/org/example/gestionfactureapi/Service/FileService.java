package org.example.gestionfactureapi.Service;

import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.*;
import org.example.gestionfactureapi.pdf.Inventaire.InventoryPDFGenerator;
import org.example.gestionfactureapi.pdf.PDFGenerationV;
import org.example.gestionfactureapi.Repository.FileRepository;
import org.example.gestionfactureapi.pdf.PDFGeneration;
import org.example.gestionfactureapi.pdf.bonLivV.BonLivVPDFGenerator;
import org.example.gestionfactureapi.pdf.devis.DevisPdfGenerator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;


    public void createAndSavePDF(BonCmdA bonCmdA) throws DocumentException, IOException, URISyntaxException {
        PDFGeneration pdfGeneration = new PDFGeneration(bonCmdA);
        byte[] pdfData = pdfGeneration.run();
        File file = new File(pdfData, "bonCmdAchat"+bonCmdA.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public void createAndSavePDF(BonLivA bonLivA) throws DocumentException, IOException, URISyntaxException {
        PDFGeneration pdfGeneration = new PDFGeneration(bonLivA);
        byte[] pdfData = pdfGeneration.run();
        File file = new File(pdfData, "bonLivAchat"+bonLivA.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public void createAndSavePDF(BonLivV bonLivV) throws DocumentException, IOException, URISyntaxException {
        PDFGenerationV pdfGeneration = new PDFGenerationV(bonLivV);
        byte[] pdfData = pdfGeneration.run();
        File file = new File(pdfData, "bonLivVente"+bonLivV.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public void createAndSavePDF(Devis devis) throws DocumentException, IOException, URISyntaxException {
        PDFGenerationV pdfGeneration = new PDFGenerationV(devis);
        byte[] pdfData = pdfGeneration.run();
        File file = new File(pdfData, "devisVente"+devis.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public void createAndSavePDF(FactureA factureA) throws DocumentException, IOException, URISyntaxException {
        PDFGeneration pdfGeneration = new PDFGeneration(factureA);
        byte[] pdfData = pdfGeneration.run();
        File file = new File(pdfData, "factureAchat"+factureA.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public void createAndSavePDF(FactureV factureV) throws DocumentException, IOException, URISyntaxException {
        PDFGenerationV pdfGeneration = new PDFGenerationV(factureV);
        byte[] pdfData = pdfGeneration.run();
        File file = new File(pdfData, "factureVente"+factureV.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public void createAndSavePDF(List<Stock> articles) {
        InventoryPDFGenerator pdfGenerator = new InventoryPDFGenerator();
        byte[] pdfData = pdfGenerator.run(articles);

        if (pdfData != null) {
            File file = new File(pdfData, "Inventory.pdf", "application/pdf");
            fileRepository.save(file);
        } else {
            System.out.println("Failed to generate the PDF.");
        }
    }
    public File findByFileName(String fileName) {
        return fileRepository.findByFileName(fileName).orElseThrow(
                ()->new EntityNotFoundException("File Not Found")
        );
    }
}