package logic.testcase.events;

import models.EventParams;
import models.webpackage.Element;
import models.webpackage.Locator;

import java.sql.SQLException;
import java.util.Objects;

public class CheckFieldsPresenceByQueryResultEvent extends EventWrapper {
    private String pattern;
    private String request;

    public CheckFieldsPresenceByQueryResultEvent(EventParams params, String pattern, String request) {
        super(params);
        this.pattern = pattern;
        this.request = request;
    }

    @Override
    protected void doWork() throws SQLException {
        if (Objects.isNull(pattern) || pattern.equals("") || !pattern.contains("$")) {
            pattern = ".//*[text()[contains(normalize-space(.), \"$\")]]";
        }

        dataBaseService().getQueryAllResults(request).parallelStream()
                .forEach(o -> {
                    Element element = new Element();
                    String locator = pattern.split("[$]")[0] + o + pattern.split("[$]")[1];
                    element.setLocator(new Locator(locator, (byte) 2));

                    System.out.println("        проверка наличия элемента на форме " + element.getLocator());
                    seleniumServise().findSingleVisibleElement(element);
                });

    }
}
