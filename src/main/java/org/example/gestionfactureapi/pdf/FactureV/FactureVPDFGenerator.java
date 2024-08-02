package org.example.gestionfactureapi.pdf.FactureV;

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
public class FactureVPDFGenerator {
    private Devis bon;
    private List<BonLivV> bonCmds;
    private double baseTVA;
    private double taux;
    private String name;
    private Date date;
    private Integer numero;

    public FactureVPDFGenerator(FactureV factureV) {
        if (factureV.getBonLivVS() == null) {
            this.bon = new Devis(factureV.getId(), factureV.getClient(), factureV.getItems(), factureV.getDateCreation(), factureV.getSte(), false);
        } else {
            this.bonCmds = factureV.getBonLivVS();
            List<Item> items = new ArrayList<>();
            for (BonLivV bonx : factureV.getBonLivVS()) {
                items.addAll(bonx.getDevis() != null ? bonx.getDevis().getItems() : bonx.getItems());
            }
            this.bon = new Devis(factureV.getId(), factureV.getBonLivVS().get(0).getClient(), items, factureV.getDateCreation(), factureV.getSte(), false);
        }
        this.date = factureV.getDateCreation();
        this.name = "Facture n°";
        this.numero = factureV.getId();
    }

    public byte[] run() throws DocumentException, IOException, URISyntaxException {
        Document doc = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, byteArrayOutputStream);
        doc.open();

        addCompanyLogo(doc);
        addHeaderInformation(doc);

        PdfPTable table = createTable();
        fillTable(table);
        doc.add(table);

        PdfPTable customTable = createCustomTable();
        addCustomRow(customTable);
        doc.add(customTable);

        doc.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void addCompanyLogo(Document doc) throws DocumentException, IOException {
        Image img = Image.getInstance("https://raw.githubusercontent.com/essayeswajih/BManagerRestApi/main/src/main/resources/pdf/logo1.png");
        img.scaleToFit(460, 100);
        img.setAlignment(Element.ALIGN_CENTER);

        PdfPTable logoTable = new PdfPTable(1);
        logoTable.setWidthPercentage(100);
        logoTable.addCell(createImageCell(img));
        doc.add(logoTable);
    }

    private PdfPCell createImageCell(Image img) {
        PdfPCell cell = new PdfPCell(img);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private void addHeaderInformation(Document doc) throws DocumentException {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{2, 2});

        PdfPCell companyCell = createCompanyCell(normalFont);
        PdfPCell clientCell = createClientCell(normalFont, boldFont);

        headerTable.addCell(companyCell);
        headerTable.addCell(clientCell);

        doc.add(headerTable);

        Paragraph additionalInfo = new Paragraph();
        additionalInfo.add(new Chunk("\n" + this.name + " : " + this.numero + " \n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
        additionalInfo.add(new Chunk("Date : " + this.date + "\n", boldFont));
        doc.add(additionalInfo);
    }

    private PdfPCell createCompanyCell(Font normalFont) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph("MF : " + this.bon.getSte().getMatriculeFiscale(), normalFont));
        cell.addElement(new Paragraph("Adresse : " + this.bon.getSte().getAdresse(), normalFont));
        cell.addElement(new Paragraph("Tel : " + this.bon.getSte().getFax() + " / " + this.bon.getSte().getTel(), normalFont));
        cell.addElement(new Paragraph("Email : " + this.bon.getSte().getEmail(), normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(10);
        cell.setPaddingTop(0);
        return cell;
    }

    private PdfPCell createClientCell(Font normalFont, Font boldFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        cell.setPaddingLeft(50);
        cell.addElement(new Paragraph("Client : " + this.bon.getClient().getName(), boldFont));
        cell.addElement(new Paragraph("MF : " + this.bon.getClient().getMatriculeFiscale(), normalFont));
        cell.addElement(new Paragraph("Adresse : " + this.bon.getClient().getAdresse(), normalFont));
        cell.addElement(new Paragraph("Tel : " + this.bon.getClient().getFax() + " / " + this.bon.getClient().getTel(), normalFont));
        cell.addElement(new Paragraph("Email : " + this.bon.getClient().getEmail(), normalFont));
        return cell;
    }

    private PdfPTable createTable() throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setWidths(new int[]{2, 4, 1, 1, 2, 1, 2, 1});
        addTableHeader(table);
        return table;
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        BaseColor headerColor = BaseColor.DARK_GRAY;

        Stream.of("Référence", "Désignation", "Qte", "U", "P.U.HT", "Rem %", "Total Net HT", "TVA")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell(new Phrase(title, headerFont));
                    header.setBackgroundColor(headerColor);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setBorder(PdfPCell.BOX);
                    table.addCell(header);
                });
    }

    private void fillTable(PdfPTable table) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        double index = 0;
        double tva = 0;
        for (Item item : this.bon.getItems()) {
            index++;
            addRow(table, item, cellFont);
            this.baseTVA += item.getTotalNet();
            tva += item.getArticle().getTva();
        }
        if (this.bon.getItems().size() < 10) {
            for (int i = 0; i < 10 - this.bon.getItems().size(); i++) {
                addEmptyRow(table, cellFont);
            }
        }
        this.taux = tva / index;
    }

    private void addRow(PdfPTable table, Item item, Font cellFont) {
        table.addCell(createCell(item.getArticle().getRefArticle(), cellFont, Element.ALIGN_CENTER));
        table.addCell(createCell(item.getArticle().getDesignation(), cellFont, Element.ALIGN_LEFT));
        table.addCell(createCell(String.valueOf(item.getQte()), cellFont, Element.ALIGN_CENTER));
        table.addCell(createCell(item.getArticle().getUnite(), cellFont, Element.ALIGN_CENTER));
        table.addCell(createCell(String.format("%.2f", item.getNewVenteHT()), cellFont, Element.ALIGN_RIGHT));
        table.addCell(createCell(String.format("%.2f", item.getRemise()), cellFont, Element.ALIGN_RIGHT));
        table.addCell(createCell(String.format("%.2f", item.getTotalNet()), cellFont, Element.ALIGN_RIGHT));
        table.addCell(createCell(item.getArticle().getTva().toString(), cellFont, Element.ALIGN_RIGHT));
    }

    private void addEmptyRow(PdfPTable table, Font cellFont) {
        for (int i = 0; i < 8; i++) {
            table.addCell(createCell("", cellFont, Element.ALIGN_CENTER));
        }
    }

    private PdfPCell createCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        return cell;
    }

    private PdfPTable createCustomTable() throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 2, 1, 1, 2, 2, 2, 2});
        return table;
    }

    private void addCustomRow(PdfPTable table) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        addCustomRowEntry(table, "Montant HT", String.format("%.2f", this.baseTVA), cellFont);
        addCustomRowEntry(table, "Montant TVA", String.format("%.2f", this.baseTVA * this.taux / 100), cellFont);
        addCustomRowEntry(table, "Montant TTC", String.format("%.2f", this.baseTVA + (this.baseTVA * this.taux / 100)), cellFont);
    }

    private void addCustomRowEntry(PdfPTable table, String label, String value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(label, font));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(value, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
