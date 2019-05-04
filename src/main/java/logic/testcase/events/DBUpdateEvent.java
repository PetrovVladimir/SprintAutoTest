package logic.testcase.events;

import models.EventParams;

import java.sql.SQLException;

public class DBUpdateEvent extends EventWrapper {
    private String query;

    public DBUpdateEvent(EventParams params, String query) {
        super(params);
        this.query = query;
    }

    @Override
    protected void doWork() throws SQLException {
        System.out.println("        выполнение апдейта " + query);
        if (dataBaseService().update(query) <= 0) {
            System.out.println("Апдейт " + query + " не выполнен");
        }
    }
}
