package org.example.gestionfactureapi.pdf.Inventaire;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.example.gestionfactureapi.Entity.Article;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class InventoryPDFGenerator {

    public byte[] run(List<Article> articleList) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.ORANGE);
            Paragraph title = new Paragraph("Votre inventaire", titleFont);
            title.setAlignment(Element.ALIGN_LEFT);
            document.add(title);

            // Add date and value section
            PdfPTable dateValueTable = new PdfPTable(2);
            dateValueTable.setWidths(new int[]{1, 1});
            dateValueTable.setWidthPercentage(50);
            dateValueTable.setSpacingBefore(20f);
            dateValueTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            dateValueTable.addCell(createCell("En date du :", Element.ALIGN_LEFT, FontFactory.getFont(FontFactory.HELVETICA, 12)));
            dateValueTable.addCell(createCell("28/04/2023", Element.ALIGN_RIGHT, FontFactory.getFont(FontFactory.HELVETICA, 12)));

            dateValueTable.addCell(createCell("Valeur € :", Element.ALIGN_LEFT, FontFactory.getFont(FontFactory.HELVETICA, 12)));
            dateValueTable.addCell(createCell("654,50", Element.ALIGN_RIGHT, FontFactory.getFont(FontFactory.HELVETICA, 12)));

            document.add(dateValueTable);

            // Add logo
            PdfPTable logoTable = new PdfPTable(1);
            logoTable.setWidthPercentage(50);
            logoTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoTable.setSpacingBefore(0f);
            logoTable.setSpacingAfter(10f);

            Paragraph logoText = new Paragraph("Tout pour LE RESTO", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK));
            logoText.setAlignment(Element.ALIGN_CENTER);
            PdfPCell logoCell = new PdfPCell(logoText);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoTable.addCell(logoCell);

            document.add(logoTable);

            // Add inventory table
            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);
            table.setSpacingAfter(20f);

            // Table headers
            String[] headers = {"Code", "Emplacement", "Catégorie", "Produit", "Unité", "Prix (HT)", "Nb", "Montant", "Fournisseur", "Remarques", "Seuil d'alerte stock", "Alerte stock?"};
            for (String header : headers) {
                table.addCell(createCell(header, Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            }

            // Add data rows (Assuming articleList contains the necessary data)
            /*for (Article article : articleList) {
                table.addCell(createCell(article.getCode(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getEmplacement(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getCategorie(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getProduit(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getUnite(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(String.valueOf(article.getPrixHT()), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(String.valueOf(article.getNb()), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getMontant(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getFournisseur(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getRemarques(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getSeuilAlerteStock(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(article.getAlerteStock(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            }*/

            document.add(table);
            document.close();

            // Return the generated PDF as a byte array
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PdfPCell createCell(String content, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}