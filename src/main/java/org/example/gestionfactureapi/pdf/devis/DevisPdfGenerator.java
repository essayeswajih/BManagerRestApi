package org.example.gestionfactureapi.pdf.devis;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
@Component
@RequiredArgsConstructor
public class DevisPdfGenerator {
    private Devis bon;
    private List<BonLivA> bonCmds;
    private double baseTVA;
    private double taux;

    private String name;

    private Date date;
    private Integer numero;


    public DevisPdfGenerator(Devis devis) {
        this.bon = devis;
        this.name = "Devis n°";
        this.numero = devis.getId();
        this.date = devis.getDateCreation();
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
            tva += i.getArticle().getTva()!=null ? i.getArticle().getTva() : 19;
        }
        if(this.bon.getItems().size()<10){
            int rows = 10-this.bon.getItems().size();
            for(int xxx = 0; xxx< rows;xxx++){
                addRow1(table);
            }

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
        Image img = Image.getInstance("https://raw.githubusercontent.com/essayeswajih/BManagerRestApi/main/src/main/resources/pdf/logo1.png");  // Replace with the path to your logo image
        img.scaleToFit(460, 100);  // Scale the image to fit the desired size
        img.setAlignment(Element.ALIGN_CENTER);
        PdfPTable logoTable = new PdfPTable(1);
        logoTable.setWidthPercentage(100);
        logoTable.setSpacingBefore(0);
        logoTable.setSpacingAfter(0);
        logoTable.addCell(getCellWithImage(img));
        doc.add(logoTable);
    }

    private static PdfPCell getCellWithImage(Image img) {
        PdfPCell cell = new PdfPCell(img);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private void addHeaderInformation(Document doc) throws DocumentException, IOException {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(0);
        headerTable.setWidths(new int[]{2, 2});

        PdfPCell companyCell = new PdfPCell();
        //companyCell.addElement(new Paragraph(this.bon.getSte().getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
        companyCell.addElement(new Paragraph("MF :" + this.bon.getSte().getMatriculeFiscale(), normalFont));
        companyCell.addElement(new Paragraph("Adresse :" + this.bon.getSte().getAdresse(), normalFont));
        companyCell.addElement(new Paragraph("Tel :" + this.bon.getSte().getFax() + " / " + this.bon.getSte().getTel(), normalFont));
        companyCell.addElement(new Paragraph("Email :" + this.bon.getSte().getEmail(), normalFont));
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.setPadding(10);
        companyCell.setPaddingTop(0);

        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.setPadding(5);
        clientCell.setPaddingLeft(50);
        clientCell.addElement(new Paragraph("Client : " + this.bon.getClient().getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));
        clientCell.addElement(new Paragraph("MF :" + this.bon.getClient().getMatriculeFiscale(), normalFont));
        clientCell.addElement(new Paragraph("Adresse :" + this.bon.getClient().getAdresse(), normalFont));
        clientCell.addElement(new Paragraph("Tel :" + this.bon.getClient().getFax() + " / " + this.bon.getClient().getTel(), normalFont));
        clientCell.addElement(new Paragraph("Email :" + this.bon.getClient().getEmail(), normalFont));

        headerTable.addCell(companyCell);
        headerTable.addCell(clientCell);

        // Add the header table to the document
        doc.add(headerTable);

        // Additional information
        Paragraph additionalInfo = new Paragraph();
        additionalInfo.add(new Chunk("\n"+this.name+" : " + this.numero + " \n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK)));
        additionalInfo.add(new Chunk("Date : " + this.date + "\n", boldFont));
        doc.add(additionalInfo);
    }

    private static void tableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        BaseColor headerColor = BaseColor.DARK_GRAY;

        Stream.of("Référence", "Désignation", "Qte", "U", "P.U.HT", "Rem %", "Total Net HT", "TVA")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(headerColor);
                    header.setBorder(PdfPCell.BOX);
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }
    private void addRow1(PdfPTable table) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (int i1 = 0; i1 < 8; i1++) {
            PdfPCell cell = new PdfPCell(new Phrase("", cellFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(10);
            cell.setFixedHeight(40);
            table.addCell(cell);
        }
        table.setTotalWidth(100);
    }


    private static void addRow(PdfPTable table, Item i) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        List<String> ligneDetails = new ArrayList<>();
        ligneDetails.add((String) i.getArticle().getRefArticle());
        ligneDetails.add((String) i.getArticle().getDesignation());
        ligneDetails.add(i.getQte().toString());
        ligneDetails.add(i.getArticle().getUnite());
        ligneDetails.add(String.format("%.3f", i.getNewVenteHT()));
        ligneDetails.add(i.getRemise().toString());
        double venteHT = i.getNewVenteHT();
        int qte = i.getQte();
        double remise = i.getRemise();  // Assuming getRemise() returns the discount percentage
        double totalHT = venteHT * qte;
        double discountedTotalHT = totalHT - (totalHT * remise / 100);
        ligneDetails.add(String.format("%.3f", discountedTotalHT));
        int tva = i.getArticle().getTva()!=null ? i.getArticle().getTva() : 19;
        ligneDetails.add(String.valueOf(tva));
        for (int i1 = 0; i1 < 8; i1++) {
            PdfPCell cell = new PdfPCell(new Phrase(ligneDetails.get(i1), cellFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.BOX);
            cell.setPadding(10);
            table.addCell(cell);
        }
        table.setTotalWidth(100);
    }

    private void addCustomRow(PdfPTable table) {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        // Column 1: Base T.V.A.
        PdfPCell cell = new PdfPCell(new Phrase("Base T.V.A: \n " + String.format("%.3f", (this.baseTVA)), boldFont));
        cell.setColspan(1);
        table.addCell(cell);

        // Column 2: Taux
        cell = new PdfPCell(new Phrase("Taux: " + String.valueOf(this.taux), boldFont));
        cell.setColspan(1);
        table.addCell(cell);

        // Column 3: Montant T.V.A.
        cell = new PdfPCell(new Phrase("Mont T.V.A:\n" + String.format("%.3f", (this.baseTVA * (this.taux / 100))), boldFont));
        cell.setColspan(2);
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
        table.setSpacingBefore(20);
    }
}
