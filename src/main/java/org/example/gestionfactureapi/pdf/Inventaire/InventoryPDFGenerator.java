package org.example.gestionfactureapi.pdf.Inventaire;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.example.gestionfactureapi.Entity.Article;
import org.example.gestionfactureapi.Entity.Stock;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class InventoryPDFGenerator {

    public byte[] run(List<Stock> stockList) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            double montTotal = 0;
            for (Stock stock : stockList) {
                montTotal+= stock.getArticle().getAchatHT()*stock.getQte();
            }
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
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(date);
            dateValueTable.addCell(createCell("En date du :", Element.ALIGN_LEFT, FontFactory.getFont(FontFactory.HELVETICA, 12)));
            dateValueTable.addCell(createCell(formattedDate, Element.ALIGN_RIGHT, FontFactory.getFont(FontFactory.HELVETICA, 12)));

            dateValueTable.addCell(createCell("Valeur DT :", Element.ALIGN_LEFT, FontFactory.getFont(FontFactory.HELVETICA, 12)));
            dateValueTable.addCell(createCell(String.format("%.3f",montTotal), Element.ALIGN_RIGHT, FontFactory.getFont(FontFactory.HELVETICA, 12)));

            document.add(dateValueTable);

            // Add logo
            PdfPTable logoTable = new PdfPTable(1);
            logoTable.setWidthPercentage(50);
            logoTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoTable.setSpacingBefore(0f);
            logoTable.setSpacingAfter(10f);

            Paragraph logoText = new Paragraph("Tout pour "+stockList.getFirst().getSte().getName().toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK));
            logoText.setAlignment(Element.ALIGN_CENTER);
            PdfPCell logoCell = new PdfPCell(logoText);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoTable.addCell(logoCell);

            document.add(logoTable);

            // Add inventory table
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 1, 5, 2, 1, 2, 1, 1, 1});
            table.setSpacingBefore(20f);
            table.setSpacingAfter(20f);

            // Table headers
            String[] headers = {"Code", "local", "Designation", "Prix A(HT)", "Qte", "Montant", "RMQ", "Seuil d'alerte", "Alerte stock?"};
            for (String header : headers) {
                table.addCell(createCell(header, Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            }

            // Add data rows (Assuming articleList contains the necessary data)
            for (Stock stock : stockList) {
                table.addCell(createCell(stock.getArticle().getRefArticle(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(stock.getArticle().getFamille().getAdresse(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(stock.getArticle().getDesignation(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(String.format("%.3f",stock.getArticle().getAchatHT()), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(String.valueOf(stock.getQte()), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(String.format("%.3f",stock.getArticle().getAchatHT()*stock.getQte()), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell("", Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(stock.getArticle().getSeuilStock().toString(), Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(createCell(stock.getQte()<stock.getArticle().getSeuilStock()?"Alert !!!":"", Element.ALIGN_CENTER, FontFactory.getFont(FontFactory.HELVETICA, 10,BaseColor.RED)));
            }

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
    private PdfPCell createCell(String content, int alignment,int colSpan, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setColspan(colSpan);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}
