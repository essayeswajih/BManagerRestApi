package org.example.gestionfactureapi.pdf.FactureV;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
public class FactureVPDFGenerator {
    private int x;
    private Devis bon;
    private List<BonLivV> bonCmds;
    private double baseTVA;
    private double taux;
    private String name;
    private Date date;
    private Integer numero;

    public FactureVPDFGenerator(FactureV factureV) {
        if (factureV.getBonLivVS() == null) {
            this.bon = new Devis();
            this.bon.setId(factureV.getId());
            this.bon.setItems(factureV.getItems());
            this.bon.setSte(factureV.getSte());
            this.bon.setClient(factureV.getClient());
        } else {
            this.bonCmds = factureV.getBonLivVS();
            List<Item> items = new ArrayList<>();
            for (BonLivV bonx : factureV.getBonLivVS()) {
                if (bonx.getDevis() == null) {
                    items.addAll(bonx.getItems());
                } else {
                    items.addAll(bonx.getDevis().getItems());
                }
            }
            this.bon = new Devis(factureV.getId(), factureV.getBonLivVS().get(0).getClient(), items, factureV.getDateCreation(), factureV.getSte(), false);
        }
        this.date = factureV.getDateCreation();
        this.name = "Facture n°";
        this.numero = factureV.getId();
        this.x = 3;
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
        if (this.bon.getItems().size() < 10) {
            int rows = 10 - this.bon.getItems().size();
            for (int xxx = 0; xxx < rows; xxx++) {
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
        logoTable.addCell(getCellWithImage(img, PdfPCell.ALIGN_CENTER));  // Center the logo

        doc.add(logoTable);
    }

    private static PdfPCell getCellWithImage(Image img, int alignment) {
        PdfPCell cell = new PdfPCell(img);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private void addHeaderInformation(Document doc) throws DocumentException {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(0);
        headerTable.setWidths(new int[]{2, 2});

        PdfPCell companyCell = new PdfPCell();
        companyCell.addElement(new Paragraph("MF : " + this.bon.getSte().getMatriculeFiscale(), normalFont));
        companyCell.addElement(new Paragraph("Adresse : " + this.bon.getSte().getAdresse(), normalFont));
        companyCell.addElement(new Paragraph("Tel : " + this.bon.getSte().getFax() + " / " + this.bon.getSte().getTel(), normalFont));
        companyCell.addElement(new Paragraph("Email : " + this.bon.getSte().getEmail(), normalFont));
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.setPadding(10);
        companyCell.setPaddingTop(0);

        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.setPadding(5);
        clientCell.setPaddingLeft(50);
        clientCell.addElement(new Paragraph("Client : " + this.bon.getClient().getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));
        clientCell.addElement(new Paragraph("MF : " + this.bon.getClient().getMatriculeFiscale(), normalFont));
        clientCell.addElement(new Paragraph("Adresse : " + this.bon.getClient().getAdresse(), normalFont));
        clientCell.addElement(new Paragraph("Tel : " + this.bon.getClient().getFax() + " / " + this.bon.getClient().getTel(), normalFont));
        clientCell.addElement(new Paragraph("Email : " + this.bon.getClient().getEmail(), normalFont));

        headerTable.addCell(companyCell);
        headerTable.addCell(clientCell);

        // Add the header table to the document
        doc.add(headerTable);

        // Additional information
        Paragraph additionalInfo = new Paragraph();
        additionalInfo.add(new Chunk("\n" + this.name + " : " + this.numero + " \n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK)));
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
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void addRow(PdfPTable table, Item item) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(item.getArticle().getRefArticle(), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(item.getArticle().getDesignation(), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.valueOf(item.getQte()), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(item.getArticle().getUnite(), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.format("%.2f", item.getNewVenteHT()), cellFont)); //ezeeeeeeeeeee
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.format("%.2f", item.getRemise()), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.format("%.2f", item.getTotalNet()), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.format("%.2f", item.getArticle().getTva()), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addCustomRow(PdfPTable table) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase("Montant HT", cellFont));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.format("%.2f", this.baseTVA), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Montant TVA", cellFont));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.format("%.2f", this.baseTVA * this.taux / 100), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Montant TTC", cellFont));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.format("%.2f", this.baseTVA + (this.baseTVA * this.taux / 100)), cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
