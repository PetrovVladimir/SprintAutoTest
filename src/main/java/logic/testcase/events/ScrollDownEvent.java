package logic.testcase.events;

import models.EventParams;
import models.webpackage.Element;

import java.util.Optional;

public class ScrollDownEvent extends EventWrapper {
    private Element element;

    public ScrollDownEvent(EventParams params, Element element) {
        super(params);
        this.element = element;
    }

    @Override
    protected void doWork() {
        System.out.println("        скрол вниз от элемента " + Optional.ofNullable(element.getName()).orElse(element.getLocator()));
        seleniumServise().moveMouseToElement(element);
        seleniumServise().click(element); //для навода указателя
        seleniumServise().pressKey("\ue010"); //press END
    }
}
