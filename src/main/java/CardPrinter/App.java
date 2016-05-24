package CardPrinter;

import com.riaancornelius.cardprinter.PdfTest;
import com.riaancornelius.cardprinter.jira.Ticket;
import com.riaancornelius.cardprinter.jira.TicketImporter;

import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        PdfTest pdfTest = new PdfTest();
        try {
            if (pdfTest.openPdf()) {
                List<Ticket> tickets = TicketImporter.importFrom("tickets.csv");
                tickets.addAll(TicketImporter.importFrom("subtasks.csv"));
                pdfTest.generatePdf(tickets);
                pdfTest.closePdf();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
