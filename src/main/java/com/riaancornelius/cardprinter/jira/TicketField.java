package com.riaancornelius.cardprinter.jira;

/**
 * Created by Riaan on 2014/08/17.
 */
public enum TicketField {
    ID("Key"),
    EPIC("Epic Link"),
    PARENT_ID("parent"),
    DEVSCHED_ID("DevShed ID"),
    ISSUE_TYPE("Issue Type"),
    POINTS("Story Points"),
    TIME("Original Estimate"),
    VERSION("Version"),
    TITLE("Summary"),
    DESCRIPTION("Description");

    private final String stringRepresentation;

    TicketField(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static TicketField parse(String parse){
        for (TicketField field : TicketField.values()) {
            if (field.stringRepresentation.equals(parse)) {
                return field;
            }
        }
        return null;
    }
}
