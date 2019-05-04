package logic.testcase.events;

import models.EventParams;
import models.webpackage.Element;

import java.util.Optional;

public class ClickElementEvent extends EventWrapper {
    private Element element;

    public ClickElementEvent(EventParams params, Element element) {
        super(params);
        this.element = element;
    }

    @Override
    protected void doWork() {
        System.out.println("        клик по элементу " + Optional.ofNullable(element.getName()).orElse(element.getLocator()));
        seleniumServise().click(element);
    }
}
