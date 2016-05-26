package CardPrinter;

import com.riaancornelius.cardprinter.PdfTest;
import com.riaancornelius.cardprinter.jira.Ticket;
import com.riaancornelius.cardprinter.jira.TicketImporter;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    // command line flags
    private static final String TICKET_FLAG = "t";
    private static final String SUBTASK_FLAG = "s";
    private static final String OUTPUT_FLAG = "o";
    private static final String HELP_FLAG = "h";

    //defaults
    private static final String DEFAULT_SUBTASKS_FILE = "subtasks.csv";
    private static final String DEFAULT_TICKETS_FILE = "tickets.csv";
    private static final String DEFAULT_OUTPUT_FILE = "output.pdf";

    /**
     * @return available command line options.
     */
    private static Options getOptions() {
        Options options = new Options();
        options.addOption(TICKET_FLAG, true, "the tickets file. Default: " + DEFAULT_TICKETS_FILE);
        options.addOption(SUBTASK_FLAG, true, "the subtasks file. Default: " + DEFAULT_SUBTASKS_FILE);
        options.addOption(OUTPUT_FLAG, true, "the output file. Default: " + DEFAULT_OUTPUT_FILE);
        options.addOption(HELP_FLAG, false, "print help");
        return options;
    }

    private static class CmdOptions {
        boolean showHelp = false;
        String tickets = null;
        String subtasks = null;
        String output = null;
    }

    private static CmdOptions parseOptions(String[] args) throws ParseException {
        CmdOptions options = new CmdOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine commands = parser.parse(getOptions(), args);

        options.showHelp = commands.hasOption(HELP_FLAG);
        options.tickets = commands.getOptionValue(TICKET_FLAG, null);
        options.subtasks = commands.getOptionValue(SUBTASK_FLAG, null);
        options.output = commands.getOptionValue(OUTPUT_FLAG, null);

        return options;
    }

    public static void main(String[] args) {
        try {
            CmdOptions opts = parseOptions(args);
            if (opts.showHelp) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("ant", getOptions());
            } else {
                createTicketsPdf(opts.output != null ? opts.output : DEFAULT_OUTPUT_FILE,
                        opts.tickets != null ? opts.tickets : DEFAULT_TICKETS_FILE,
                        opts.subtasks != null ? opts.subtasks : DEFAULT_SUBTASKS_FILE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void createTicketsPdf(String pdfFileName, String ticketFile, String subTaskFile) {
        PdfTest pdfTest = new PdfTest();
        try {
            if (pdfTest.openPdf(pdfFileName)) {
                List<Ticket> tickets = new ArrayList<Ticket>();
                tickets.addAll(TicketImporter.importFrom(ticketFile));
                tickets.addAll(TicketImporter.importFrom(subTaskFile));
                pdfTest.generatePdf(tickets);
                pdfTest.closePdf();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
