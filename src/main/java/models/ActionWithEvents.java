package models;

import java.util.List;

public class ActionWithEvents {
    private String action;
    private List<String> eventList;

    public ActionWithEvents(String action, List<String> eventList) {
        this.action = action;
        this.eventList = eventList;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getEventList() {
        return eventList;
    }

}
