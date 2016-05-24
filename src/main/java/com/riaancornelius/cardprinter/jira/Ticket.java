package com.riaancornelius.cardprinter.jira;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Riaan on 2014/08/17.
 */
public class Ticket {

    private final String id;
    private final String projectCode;
    private String epic;
    private String parentId;
    private String devschedId;
    private String type;
    private String title;
    private String description;
    private String points;
    private String time;
    private String version;

    public Ticket(String id) {
        this.id = id;
        projectCode = id.replaceAll("-\\d+", "");
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public String getEpic() {
        return epic;
    }

    public void setEpic(String epic) {
        this.epic = epic;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDevschedId() {
        return devschedId;
    }

    public void setDevschedId(String devschedId) {
        this.devschedId = devschedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        if (!isEmpty(title) && !isEmpty(getParentId())) {
            return title.replaceFirst(getParentId(),"").trim();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (!isEmpty(getProjectCode()) && !isEmpty(title)) {
            Pattern pattern = Pattern.compile(getProjectCode() + "-\\d+ ", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(getTitle());
            // check all occurance
            if (matcher.find()) {
                parentId = title.substring(matcher.start(), matcher.end()).trim();
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static boolean isEmpty(String string) {
        return string==null || string.isEmpty() || string.equals("null");
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", points='" + points + '\'' +
                ", time='" + time + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public static Ticket generateTest() {
        Ticket ticket = new Ticket("FLUX-48");
        ticket.setDescription("Some long description goes here... elkhf ilw fhweifewhifeli e weilf ehwl fewl e iwl fle hfew hlefwihl fweih lfewhif elhi fhwel ihfel ...and ends here");
        ticket.setParentId("FLUX-46");
        ticket.setPoints("3");
        ticket.setTime("6h");
        ticket.setVersion("Apricot");
        ticket.setTitle("Some title will go here. Let's just make it somewhat longer!");
        return ticket;
    }


}
