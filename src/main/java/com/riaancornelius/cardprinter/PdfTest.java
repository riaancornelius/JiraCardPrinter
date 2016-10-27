package com.riaancornelius.cardprinter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.riaancornelius.cardprinter.jira.Ticket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;

public class PdfTest {

    private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static Font detailFont = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    private static Font detailBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);

    private Document document;

    public PdfTest() {
        document = new Document(PageSize.A4);
    }

    public boolean openPdf(String pdfFileName) {
        boolean status = false;
        try {
            File pdfFile = new File(pdfFileName);
            if (pdfFile != null) {
                PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                document.open();
                status = true;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public void closePdf() {
        document.close();
    }

    public void generatePdf(java.util.List<Ticket> tickets) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        PdfPCell cell = null;
        // Main table
        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100.0f);

        for (Ticket ticket : tickets) {
            mainTable.addCell(generateCard(ticket));
        }

        paragraph.add(mainTable);
        document.add(paragraph);
    }

    private PdfPCell generateCard(Ticket ticket) {
        PdfPCell firstTableCell = new PdfPCell();
        firstTableCell.setBorderWidth(1f);
        firstTableCell.setPadding(5f);

        PdfPTable firstTable = new PdfPTable(2);
        firstTable.setWidthPercentage(95.0f);
        firstTable.setExtendLastRow(true);


        firstTable.addCell(createBoldDetailCell(ticket.getId(), Element.ALIGN_LEFT));

        // Add timing and points
        if (ticket.getPoints()!=null && !ticket.getPoints().isEmpty() && !ticket.getPoints().equals("null")) {
            firstTable.addCell(createBoldDetailCell("Points: " + ticket.getPoints(), Element.ALIGN_LEFT));
        } else if (ticket.getTime()!=null && !ticket.getTime().isEmpty() && !ticket.getTime().equals("null")) {
            try {
                BigDecimal time = new BigDecimal(ticket.getTime());
                BigDecimal timeInHours = time.divide(new BigDecimal(3600), BigDecimal.ROUND_HALF_UP);
                firstTable.addCell(createBoldDetailCell("Estimate: " + timeInHours.toPlainString() + "h", Element.ALIGN_LEFT));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                firstTable.addCell(createBoldDetailCell("", Element.ALIGN_LEFT));
            }
        } else {
            firstTable.addCell(createBoldDetailCell("", Element.ALIGN_LEFT));
        }
//
//        if (ticket.getVersion()!=null && !ticket.getVersion().isEmpty() && !ticket.getVersion().equals("null")) {
//            firstTable.addCell(createDetailCell("Version:", Element.ALIGN_RIGHT));
//            firstTable.addCell(createBoldDetailCell(ticket.getVersion(), Element.ALIGN_LEFT));
//        } else {
//            firstTable.addCell(createDetailCell("", Element.ALIGN_RIGHT));
//            firstTable.addCell(createBoldDetailCell("", Element.ALIGN_LEFT));
//        }

        // Blank cell to make sure title starts at the right place
//        firstTable.addCell(createDetailCell("", Element.ALIGN_RIGHT));
//        firstTable.addCell(createBoldDetailCell("", Element.ALIGN_LEFT));

        System.out.println(ticket.getTitle());
        firstTable.addCell(createTitleCell(ticket.getTitle(), 3f));
        firstTable.addCell(createDescriptionCell(ticket.getDescription()));
        firstTable.addCell(createQRCodeCell(ticket.getId()));

        //firstTable.addCell(createQRCodeCell(ticket.getId(), 80, Element.ALIGN_LEFT));

        // DevSched
        if (ticket.getDevschedId()!=null && !ticket.getDevschedId().isEmpty() && !ticket.getDevschedId().equals("null")) {
            firstTable.addCell(createDoubleWidthDetailCell("DevSched ID: " + ticket.getDevschedId(), Element.ALIGN_LEFT));
        }
        // Parent:
        if (ticket.getParentId()!=null && !ticket.getParentId().isEmpty() && !ticket.getParentId().equals("null")) {
            firstTable.addCell(createDoubleWidthDetailCell("Parent ID: " + ticket.getParentId(), Element.ALIGN_LEFT));
        }

        if (ticket.getEpic()!=null && !ticket.getEpic().isEmpty() && !ticket.getEpic().equals("null")) {
            firstTable.addCell(createDoubleWidthDetailCell("Epic: " + ticket.getEpic(), Element.ALIGN_LEFT));
        }

        firstTableCell.addElement(firstTable);

        return firstTableCell;
    }

    private PdfPCell createBoldDetailCell(String text, int alignment) {
        return createBoldDetailCell(text, alignment, false);
    }

    private PdfPCell createBoldDetailCell(String text, int alignment, boolean border) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(text, detailBoldFont));
        if (border) {
            cell.setBorderColor(BaseColor.BLACK);
            cell.setUseBorderPadding(true);
            cell.setBorderWidth(3.0f);
        } else {
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private PdfPCell createDetailCell(String text, int alignment) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(text, detailFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private PdfPCell createDoubleWidthDetailCell(String text, int alignment) {
        PdfPCell cell = createDetailCell(text, alignment);
        cell.setColspan(2);
        return cell;
    }

    private PdfPCell createTitleCell(String text, float padding) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(text, titleFont));
        cell.setPadding(padding);
        cell.setFixedHeight(45f);
        cell.setColspan(2);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell createDescriptionCell(String description) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(description, detailFont));
        //cell.setColspan(2);
        cell.setFixedHeight(60f);
        cell.setBorder(PdfPCell.NO_BORDER);
        //cell.setPaddingTop(5f);
        cell.setPaddingRight(5f);
        return cell;
    }

    private PdfPCell createQRCodeCell(String id) {
        PdfPCell cell;
        String url = "http://plpbugz01.discsrv.co.za:8080/browse/";
        Image qrCode = createQrCode( url + id, 100, 100);
        qrCode.setBorderWidth(5f);
        qrCode.setBorderColor(BaseColor.BLACK);
        cell = new PdfPCell(qrCode);
//        cell.setUseBorderPadding(true);
//        cell.setBorderColor(BaseColor.BLACK);
//        cell.setBorderWidth(5.0f);
//        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT | Element.ALIGN_TOP);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell createQRCodeCell(String id, int size, int alignment) {
        PdfPCell cell;
        String url = "http://plpbugz01.discsrv.co.za:8080/browse/";
        Image qrCode = createQrCode( url + id, size, size);
//        qrCode.enableBorderSide(com.itextpdf.text.Rectangle.BOX);
//        qrCode.setBorderWidth(5f);
//        qrCode.setBorderColor(BaseColor.BLACK);
        cell = new PdfPCell(qrCode);
//        cell.setUseBorderPadding(true);
//        cell.setBorderColor(BaseColor.BLACK);
//        cell.setBorderWidth(5.0f);
//        cell.setColspan(2);
        cell.setHorizontalAlignment(alignment | Element.ALIGN_TOP);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private Image createQrCode(String content, int width, int height){
        System.out.println("Creating QR Code for: " + content);
        BarcodeQRCode qrcode = new BarcodeQRCode(content, width, height, null);
        Image qrcodeImage = null;
        try {
            qrcodeImage = qrcode.getImage();
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        return qrcodeImage;
    }
}

