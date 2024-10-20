package org.example.gestionfactureapi.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.example.gestionfactureapi.Entity.*;
import org.example.gestionfactureapi.Service.FactureVService;
import org.example.gestionfactureapi.tools.NumberToText;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class PDFGenerationV {

    private FactureV facture;
    private  int x;
    private Devis bon;
    private List<BonLivV> bonLivVs;
    private double baseTVA19=0;
    private double montTVA19=0;
    private double baseTVA7=0;
    private double montTVA7=0;
    private double baseTVA13=0;
    private double montTVA13=0;
    private double remise = 0;
    private double totalHT=0;
    double netHT = 0;
    private double totalTTC=0;
    private double timbre = 0;
    private String name;

    private Date date;
    private Integer numero;
    static class RoundedBorder implements PdfPCellEvent {
        @Override
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setColorStroke(BaseColor.BLACK); // Border color
            cb.setColorFill(BaseColor.GRAY); // Background color
            float radius = 10; // Radius for rounded corners
            // Draw rounded rectangle
            cb.roundRectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight(), radius);
            cb.fillStroke();
            cb.restoreState();
        }
    }

    public PDFGenerationV(Devis bon) {
        this.bon = bon;
        this.numero = Integer.parseInt(bon.getRef().substring(7));
        this.date = bon.getDateCreation();
        this.name = "Bon de commande";
        this.x=1;
    }
    public PDFGenerationV(BonLivV bonLivV) {
        if(bonLivV.getDevis()==null){
            Devis bca = new Devis();
            bca.setItems(bonLivV.getItems());
            bca.setId(bonLivV.getId());
            bca.setSte(bonLivV.getSte());
            bca.setClient(bonLivV.getClient());
            this.bon = bca;
        }else {
            this.bon = bonLivV.getDevis();
        }
        this.numero = Integer.parseInt(bonLivV.getRef().substring(7));
        this.date = bonLivV.getDateCreation();
        this.name = "Bon de livraison";
        this.x=2;
    }
    public PDFGenerationV(FactureV factureV) {
        this.facture = factureV;
        if(factureV.getBonLivVS() == null){
            this.bon = new Devis();
            this.bon.setId(factureV.getId());
            this.bon.setItems(factureV.getItems());
            this.bon.setSte(factureV.getSte());
            this.bon.setClient(factureV.getClient());
        }else{
            this.bonLivVs = factureV.getBonLivVS();
            List<Item> items =new ArrayList<>();
            for(BonLivV bonx : factureV.getBonLivVS()){
                if(bonx.getDevis() == null){
                    items.addAll(bonx.getItems());
                }else {
                    items.addAll(bonx.getDevis().getItems());
                }

            }
            this.bon = new Devis(factureV.getId(),factureV.getBonLivVS().get(0).getClient(), items,factureV.getDateCreation(),factureV.getSte(),false,factureV.getRef());
        }

        this.date = factureV.getDateCreation();
        this.name = "Facture";
        this.numero = Integer.parseInt(factureV.getRef().substring(7));
        this.x=3;
        this.timbre=1;
    }


    public Object run() throws DocumentException, IOException, URISyntaxException {
        final FactureVService factureVService;
        Document doc = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(doc, byteArrayOutputStream);
        doc.open();

        // Add company logo
        addCompanyLogo(doc);

        // Add header information
        addHeaderInformation(doc);

        // Add table with data
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new int[]{2, 4, 1, 1, 2, 1, 2, 1});
        tableHeader(table);

        for (Item i : this.bon.getItems()) {
            addRow(table, i);
                int tva = i.getArticle().getTva();
                if(tva==19){
                    this.baseTVA19+=i.getTotalNet();
                    this.montTVA19+=i.getTotalNet()*.19;
                } else if (tva==7) {
                    this.baseTVA7+=i.getTotalNet();
                    this.montTVA7+=i.getTotalNet()*.7;
                } else if (tva==13) {
                    this.baseTVA13+=i.getTotalNet();
                    this.montTVA13+=i.getTotalNet()*.13;
                }
            int qte = i.getQte();
            double remise = i.getRemise() == null ? 0 : i.getRemise();
            double venteHT2 = i.getNewVenteHT() !=null ? i.getNewVenteHT() : i.getArticle().getVenteHT();
            double totalHT = venteHT2 * qte;
            this.remise += (totalHT * remise / 100);
            this.totalHT+=totalHT;

        }
        if(this.bon.getClient().isExonere() && this.x>1){
            montTVA19 = 0;
            montTVA13 =0;
            montTVA7 = 0;
        }
        this.netHT = this.totalHT - this.remise;
        this.totalTTC=this.netHT+ montTVA19 + montTVA13 + montTVA7 + this.timbre;

        doc.add(table);


        PdfPTable customTable = new PdfPTable(6);
        customTable.setWidths(new int[]{9, 9, 9, 1, 9, 9});
        customTable.setWidthPercentage(100); // Adjust the percentage as needed
        customTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        addCustomRow(customTable);  // Add custom row with image and description
        customTable.setSpacingBefore(10);
        doc.add(customTable);
        doc.close();
        if(facture!=null){
            facture.setTotalTTC(this.totalTTC);
            facture.setTimbre(timbre);
            facture.setTotal(totalHT);
            facture.setBaseTVA7(baseTVA7);
            facture.setMontTVA7(montTVA7);
            facture.setBaseTVA13(baseTVA13);
            facture.setMontTVA13(montTVA13);
            facture.setBaseTVA19(baseTVA19);

            HashMap<String,Object> res= new HashMap<>();
            res.put("facture",facture);
            res.put("byteArray",byteArrayOutputStream.toByteArray());
            return res;
        }else {
            return byteArrayOutputStream.toByteArray();
        }

    }


    private static void addCompanyLogo(Document doc) throws DocumentException, IOException {
        Image img = Image.getInstance("https://raw.githubusercontent.com/essayeswajih/BManagerRestApi/main/src/main/resources/pdf/logo1.png");  // Replace with the path to your logo image
        img.scaleToFit(460, 100);  // Scale the image to fit the desired size
        img.setAlignment(Element.ALIGN_CENTER);

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
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,BaseColor.GRAY);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10,BaseColor.GRAY);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(0);
        headerTable.setWidths(new int[]{2, 2});

        PdfPCell companyCell = new PdfPCell();
        //companyCell.addElement(new Paragraph(this.bon.getSte().getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
        companyCell.addElement(new Paragraph("Code TVA : " + this.bon.getSte().getMatriculeFiscale(), normalFont));
        companyCell.addElement(new Paragraph("Adresse : " + this.bon.getSte().getAdresse(), normalFont));
        companyCell.addElement(new Paragraph("GSM : " + this.bon.getSte().getFax() + " - " + this.bon.getSte().getTel(), normalFont));
        companyCell.addElement(new Paragraph("Email : " + this.bon.getSte().getEmail(), normalFont));
        companyCell.setBorder(0);
        companyCell.setPaddingBottom(10);
        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.setPadding(0);
        PdfPTable clientTbale = new PdfPTable(3);
        clientTbale.setWidthPercentage(100);
        addCellOfHeading(clientTbale, "Client", normalFont, 1, 1);
        addCell(clientTbale,  this.bon.getClient().getName(), normalFont, 2, 1);
        addCellOfHeading(clientTbale, "Adresse", normalFont, 1, 1);
        addCell(clientTbale, this.bon.getClient().getAdresse(), normalFont, 2, 1);
        addCellOfHeading(clientTbale, "Code TVA", normalFont, 1, 1);
        addCell(clientTbale, this.bon.getClient().getMatriculeFiscale(), normalFont, 2, 1);
        clientCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(companyCell);
        //clientTbale.setSpacingBefore(52);
        clientCell.addElement(clientTbale);
        headerTable.addCell(clientCell);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.GRAY);
        PdfPCell headingName = new PdfPCell(new Phrase(this.name,headerFont));
        headingName.setBorder(0);
        headingName.setPadding(10);
        headingName.setPaddingBottom(15);
        headingName.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        headingName.setBackgroundColor(BaseColor.WHITE);
        PdfPCellEvent roundedBorder = new PDFGeneration.RoundedBorder();
        headingName.setCellEvent(roundedBorder);
        //headerTable.addCell(headingName);
        PdfPCell headingEspace = new PdfPCell(new Phrase(""));
        headingEspace.setBorder(0);
        //headerTable.addCell(headingEspace);
        // Add the header table to the document
        doc.add(headerTable);

        // Additional information
        PdfPTable tablex = new PdfPTable(2);
        tablex.setSpacingBefore(10);
        addRowx(tablex);
        doc.add(tablex);

        //Paragraph additionalInfo = new Paragraph();
        //additionalInfo.add(new Chunk("\n"+this.name+" : " + this.numero + " \n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.GRAY)));
        //additionalInfo.add(new Chunk("Date : " + this.date + "\n", boldFont));
        //doc.add(additionalInfo);
    }
    private static void tableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.GRAY);
        BaseColor headerColor = BaseColor.LIGHT_GRAY;

        Stream.of("Référence", "Désignation", "Qte", "U", "P.U.HT", "Rem %", "Total Net HT", "TVA")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(headerColor);
                    header.setBorder(PdfPCell.BOX);
                    header.setBorderColor(BaseColor.GRAY);
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }
    private void addRow1(PdfPTable table) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10,BaseColor.BLACK);
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
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10,BaseColor.GRAY);
        List<String> ligneDetails = new ArrayList<>();
        ligneDetails.add(i.getArticle().getRefArticle());
        ligneDetails.add(i.getArticle().getDesignation());
        ligneDetails.add(i.getQte().toString());
        ligneDetails.add(i.getArticle().getUnite());
        double venteHT = i.getNewVenteHT() !=null ? i.getNewVenteHT() : i.getArticle().getVenteHT();
        ligneDetails.add(String.format("%.3f", venteHT));
        ligneDetails.add(i.getRemise() == null ? "0" :String.valueOf(i.getRemise()));
        int qte = i.getQte();
        double remise = i.getRemise() == null ? 0 : i.getRemise();  // Assuming getRemise() returns the discount percentage
        double totalHT = venteHT * qte;

        double discountedTotalHT = totalHT - (totalHT * remise / 100);
        ligneDetails.add(String.format("%.3f", discountedTotalHT));
        ligneDetails.add(i.getArticle().getTva().toString());
        for (int i1 = 0; i1 < 8; i1++) {
            PdfPCell cell = new PdfPCell(new Phrase(ligneDetails.get(i1), cellFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.BOX);
            cell.setBorderColor(BaseColor.GRAY);
            cell.setPadding(7);
            table.addCell(cell);
        }
        table.setTotalWidth(100);
    }
    private void addRowx(PdfPTable table){
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.GRAY);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.GRAY);
        addCellOfHeading(table,(this.name+" n°").toUpperCase(), headerFont);
        addCellOfHeading(table, "DATE", headerFont);
        addCell(table, ""+this.numero, normal);
        addCell(table, ""+this.date, normal);
        table.setWidthPercentage(100);
    }
    private void addCustomRow(PdfPTable table) {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,BaseColor.GRAY);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 8,BaseColor.GRAY);
        Font headerNormalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.GRAY);

        // Row 1
        addCellOfHeading(table, "Taux", headerNormalFont);
        addCellOfHeading(table, "Base" , headerNormalFont);
        addCellOfHeading(table, "TVA", headerNormalFont);
        addCellVide(table);
        addCellOfHeading(table, "TOTAL HT", headerNormalFont);
        addCell(table, String.format("%.3f",this.totalHT), normalFont);

        // Row 1
        addCell(table, "19%", normalFont);
        addCell(table, String.format("%.3f",this.baseTVA19), normalFont);
        addCell(table, String.format("%.3f",this.montTVA19), normalFont);
        addCellVide(table);
        addCellOfHeading(table,"REMISE",headerNormalFont);
        addCell(table, String.format("%.3f",this.remise), normalFont);

        // Row 1
        addCell(table, "13%", normalFont);
        addCell(table, String.format("%.3f",this.baseTVA13), normalFont);
        addCell(table, String.format("%.3f",this.montTVA13), normalFont);
        addCellVide(table);
        addCellOfHeading(table,"NET HT",headerNormalFont);
        addCell(table, String.format("%.3f",this.netHT), normalFont);

        // Row 1
        addCell(table, "7%", normalFont);
        addCell(table, String.format("%.3f",this.baseTVA7), normalFont);
        addCell(table, String.format("%.3f",this.montTVA7), normalFont);
        addCellVide(table);
        addCellOfHeading(table,"TOTAL TVA",headerNormalFont);
        double totalTVA = this.montTVA19 + this.montTVA13 + this.montTVA7;
        addCell(table,String.format("%.3f",totalTVA) , normalFont);

        NumberToText converter = new NumberToText(String.format("%.3f",this.totalTTC));
        String text="Arrétéé la présente "+this.name+" à la somme de : "+converter.toText().toUpperCase();
        addCell(table,text,boldFont,3,2,0);

        addCellVide(table);
        addCellOfHeading(table,"TIMBRE",headerNormalFont);
        addCell(table, String.format("%.3f",this.timbre), normalFont);


        addCellVide(table);
        addCellOfHeading(table,"TTC",headerNormalFont);
        addCell(table, String.format("%.3f",this.totalTTC), normalFont);

    }

    private void addCell(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(1);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.GRAY);
        table.addCell(cell);
    }
    private void addCell(PdfPTable table, String content, Font font,int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.GRAY);
        table.addCell(cell);
    }
    private void addCell(PdfPTable table, String content, Font font,int colspan,int rowlspan) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setRowspan(rowlspan);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.GRAY);
        table.addCell(cell);
    }
    private void addCell(PdfPTable table, String content, Font font,int colspan,int rowlspan,int border) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setRowspan(rowlspan);
        cell.setPadding(5);
        cell.setPaddingTop(15);
        cell.setBorder(border);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.GRAY);
        table.addCell(cell);
    }
    private void addCellVide(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
    private void addCellOfHeading(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(1);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setBorderColor(BaseColor.GRAY);
        table.addCell(cell);

    }
    private void addCellOfHeading(PdfPTable table, String content, Font font,int colspan,int rowlspan) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.GRAY);
        PdfPCell cell = new PdfPCell(new Phrase(content, headerFont));
        cell.setColspan(colspan);
        cell.setRowspan(rowlspan);
        cell.setPadding(5);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }

}
