package com.riaancornelius.cardprinter.jira;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Riaan on 2014/08/17.
 */
public class TicketImporter {

    public static List<Ticket> importFrom(String file){
        List<Ticket> list = new ArrayList<Ticket>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file));
            String [] nextLine;
            int index = 0;
            Map<TicketField, Integer> fields = new HashMap<TicketField, Integer>();
            while ((nextLine = reader.readNext()) != null) {
                if (index==0) {
                    for (int i=0; i<nextLine.length; i++) {
                        TicketField field = TicketField.parse(nextLine[i]);
                        if (field!=null) {
                            fields.put(field, i);
                        }
                    }
                } else {
                    System.out.println("Importing ticket: " + index + "(" + nextLine[1] + ")");
                    Integer keyIndex = fields.get(TicketField.ID);
                    Ticket ticket = null;
                    if (keyIndex!=null){
                        ticket = new Ticket(nextLine[keyIndex]);
                    } else {
                        System.out.println("No ID - Continuing");
                        continue;
                    }
                    Integer titleIndex = fields.get(TicketField.TITLE);
                    if (titleIndex!=null){
                        ticket.setTitle(nextLine[titleIndex]);
                    }
                    Integer epicIndex = fields.get(TicketField.EPIC);
                    if (epicIndex!=null){
                        ticket.setEpic(nextLine[epicIndex]);
                    }

                    Integer devshedIndex = fields.get(TicketField.DEVSCHED_ID);
                    if (devshedIndex!=null){
                        ticket.setDevschedId(nextLine[devshedIndex]);
                    }
                    Integer typeIndex = fields.get(TicketField.ISSUE_TYPE);
                    if (typeIndex!=null){
                        ticket.setType(nextLine[typeIndex]);
                    }
                    Integer descIndex = fields.get(TicketField.DESCRIPTION);
                    if (descIndex!=null){
                        ticket.setDescription(nextLine[descIndex]);
                    }
                    Integer pidIndex = fields.get(TicketField.PARENT_ID);
                    if (pidIndex!=null){
                        ticket.setParentId(nextLine[pidIndex]);
                    }
                    Integer pointsIndex = fields.get(TicketField.POINTS);
                    if (pointsIndex!=null){
                        ticket.setPoints(nextLine[pointsIndex]);
                    }
                    Integer timeIndex = fields.get(TicketField.TIME);
                    if (timeIndex!=null){
                        ticket.setTime(nextLine[timeIndex]);
                    }
                    Integer versionIndex = fields.get(TicketField.VERSION);
                    if (versionIndex!=null){
                        ticket.setVersion(nextLine[versionIndex]);
                    }
                    System.out.println("Imported ticket: " + ticket.toString());
                    list.add(ticket);
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
