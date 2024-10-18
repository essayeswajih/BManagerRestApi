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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FactureVService factureVService;
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
        byte[] pdfData = (byte[]) pdfGeneration.run();
        File file = new File(pdfData, "bonLivVente"+bonLivV.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public void createAndSavePDF(Devis devis) throws DocumentException, IOException, URISyntaxException {
        PDFGenerationV pdfGeneration = new PDFGenerationV(devis);
        byte[] pdfData = (byte[]) pdfGeneration.run();
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
        HashMap<String,Object> res;
        res = (HashMap<String, Object>) pdfGeneration.run();
        byte[] pdfData = (byte[]) res.get("byteArray");
        factureVService.save((FactureV) res.get("facture"));
        File file = new File(pdfData, "factureVente"+factureV.getId()+".pdf", "application/pdf");
        fileRepository.save(file);
    }
    public String createAndSavePDF(List<Stock> articles) throws IOException {
        InventoryPDFGenerator pdfGenerator = new InventoryPDFGenerator();
        byte[] pdfData = pdfGenerator.run(articles);
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

        if (pdfData != null) {
            String fileName = "Inventory_"+articles.getFirst().getSte().getName()+"_"+date.getTime()+"_"+"_.pdf";
            File file = new File(pdfData, fileName, "application/pdf");
            fileRepository.save(file);
            return fileName;
        } else {
            System.out.println("Failed to generate the PDF.");
            throw new IOException("cant create this file");
        }
    }
    public File findByFileName(String fileName) {
        return fileRepository.findByFileName(fileName).orElseThrow(
                ()->new EntityNotFoundException("File Not Found")
        );
    }
}