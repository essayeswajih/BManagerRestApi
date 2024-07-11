package org.example.gestionfactureapi.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.BonCmdA;
import org.example.gestionfactureapi.Entity.Item;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
@Component
@RequiredArgsConstructor
public class PDFGeneration {
    private BonCmdA bon;
    private double baseTVA;
    private double taux;

    public PDFGeneration(BonCmdA bon) {
        this.bon = bon;
    }

    public byte[] run() throws DocumentException, IOException, URISyntaxException {
        Document doc = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, byteArrayOutputStream);
        doc.open();

        // Add company logo
        addCompanyLogo(doc);

        // Add header information
        addHeaderInformation(doc);

        // Add table with data
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setWidths(new int[]{2, 4, 1, 1, 2, 1, 2, 1});
        tableHeader(table);
        double index = 0;
        double tva = 0;
        for (Item i : this.bon.getItems()) {
            index++;
            addRow(table, i);
            this.baseTVA += i.getTotalNet();
            tva += i.getArticle().getTva();
        }
        this.taux = tva / index;
        doc.add(table);

        // Add custom row after the table
        PdfPTable customTable = new PdfPTable(8);
        customTable.setWidthPercentage(100);
        customTable.setWidths(new int[]{2, 2, 1, 1, 2, 2, 2, 2});
        addCustomRow(customTable);  // Add custom row with image and description

        doc.add(customTable);

        doc.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static void addCompanyLogo(Document doc) throws DocumentException, IOException {
        Image img = Image.getInstance("src/main/resources/pdf/logo.png");  // Replace with the path to your logo image
        img.scaleToFit(150, 100);  // Scale the image to fit the desired size
        img.setAlignment(Element.ALIGN_LEFT);

        PdfPTable logoTable = new PdfPTable(1);
        logoTable.setWidthPercentage(100);
        logoTable.setSpacingBefore(0);
        logoTable.setSpacingAfter(0);
        logoTable.addCell(getCellWithImage(img, PdfPCell.ALIGN_LEFT));

        doc.add(logoTable);
    }

    private static PdfPCell getCellWithImage(Image img, int alignment) {
        PdfPCell cell = new PdfPCell(img);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private void addHeaderInformation(Document doc) throws DocumentException, IOException {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(20);
        headerTable.setWidths(new int[]{2, 2});

        PdfPCell companyCell = new PdfPCell();
        companyCell.addElement(new Paragraph(this.bon.getSte().getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
        companyCell.addElement(new Paragraph("MF :" + this.bon.getSte().getMatriculeFiscale(), normalFont));
        companyCell.addElement(new Paragraph("Adresse :" + this.bon.getSte().getAdresse(), normalFont));
        companyCell.addElement(new Paragraph("Tel :" + this.bon.getSte().getFax() + " / " + this.bon.getSte().getTel(), normalFont));
        companyCell.addElement(new Paragraph("Email :" + this.bon.getSte().getEmail(), normalFont));
        companyCell.setBorderWidth(1);
        companyCell.setPadding(10);
        companyCell.setPaddingTop(0);

        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.setPadding(5);
        clientCell.setPaddingLeft(50);
        clientCell.addElement(new Paragraph("Fournisseur : " + this.bon.getFournisseur().getIntitule(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));
        clientCell.addElement(new Paragraph("MF :" + this.bon.getFournisseur().getMatriculeFiscale(), normalFont));
        clientCell.addElement(new Paragraph("Adresse :" + this.bon.getFournisseur().getAdresse(), normalFont));
        clientCell.addElement(new Paragraph("Tel :" + this.bon.getFournisseur().getFax() + " / " + this.bon.getFournisseur().getTel(), normalFont));
        clientCell.addElement(new Paragraph("Email :" + this.bon.getFournisseur().getEmail(), normalFont));

        headerTable.addCell(companyCell);
        headerTable.addCell(clientCell);

        // Add the header table to the document
        doc.add(headerTable);

        // Additional information
        Paragraph additionalInfo = new Paragraph();
        additionalInfo.add(new Chunk("\nBon de commande n° : " + this.bon.getId() + " \n", boldFont));
        additionalInfo.add(new Chunk("Date : " + this.bon.getDateCreation() + "\n", boldFont));
        doc.add(additionalInfo);
    }

    private static void tableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        BaseColor headerColor = BaseColor.DARK_GRAY;

        Stream.of("Référence", "Désignation", "Qte", "U", "P.U.HT", "Rem %", "Total Net HT", "TVA")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(headerColor);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private static void addRow(PdfPTable table, Item i) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        List<String> ligneDetails = new ArrayList<>();
        ligneDetails.add((String) i.getArticle().getRefArticle());
        ligneDetails.add((String) i.getArticle().getDesignation());
        ligneDetails.add(i.getQte().toString());
        ligneDetails.add(i.getArticle().getUnite());
        ligneDetails.add(String.format("%.3f", i.getArticle().getAchatHT()));
        ligneDetails.add(i.getRemise().toString());
        double achatHT = i.getArticle().getAchatHT();
        int qte = i.getQte();
        double remise = i.getRemise();  // Assuming getRemise() returns the discount percentage
        double totalHT = achatHT * qte;
        double discountedTotalHT = totalHT - (totalHT * remise / 100);
        ligneDetails.add(String.format("%.3f", discountedTotalHT));
        ligneDetails.add(i.getArticle().getTva().toString());
        for (int i1 = 0; i1 < 8; i1++) {
            PdfPCell cell = new PdfPCell(new Phrase(ligneDetails.get(i1), cellFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addCustomRow(PdfPTable table) {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        // Column 1: Base T.V.A.
        PdfPCell cell = new PdfPCell(new Phrase("Base T.V.A: " + String.format("%.3f", (this.baseTVA)), boldFont));
        cell.setColspan(2);
        table.addCell(cell);

        // Column 2: Taux
        cell = new PdfPCell(new Phrase("Taux:\n" + String.valueOf(this.taux), boldFont));
        cell.setColspan(1);
        table.addCell(cell);

        // Column 3: Montant T.V.A.
        cell = new PdfPCell(new Phrase("Mont T.V.A:" + String.format("%.3f", (this.baseTVA * (this.taux / 100))), boldFont));
        cell.setColspan(1);
        cell.setFixedHeight(70);
        table.addCell(cell);

        // Column 4: Signature de Client
        cell = new PdfPCell(new Phrase("Signature de Client", boldFont));
        cell.setColspan(2);
        table.addCell(cell);

        // Column 5: Signature du magasinier
        cell = new PdfPCell(new Phrase("Signature du magasinier", boldFont));
        cell.setColspan(2);
        table.addCell(cell);

        // Column 6: Total HT
        cell = new PdfPCell(new Phrase("Total HT", boldFont));
        cell.setColspan(2);
        table.addCell(cell);

        // Column 7: Total T.T.C
        cell = new PdfPCell(new Phrase("Total T.T.C\n" + (this.baseTVA + (this.baseTVA * (this.taux / 100))), boldFont));
        cell.setColspan(2);
        cell.setFixedHeight(70);
        table.addCell(cell);
    }
}
