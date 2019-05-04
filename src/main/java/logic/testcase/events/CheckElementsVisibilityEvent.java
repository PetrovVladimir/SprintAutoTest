package logic.testcase.events;

import models.EventParams;
import models.webpackage.Element;

import java.util.List;
import java.util.Optional;

public class CheckElementsVisibilityEvent extends EventWrapper {
    private List<Element> elements;

    public CheckElementsVisibilityEvent(EventParams params, List<Element> elements) {
        super(params);
        this.elements = elements;
    }

    @Override
    protected void doWork() {
        elements.parallelStream()
                .forEach(element -> {
                    System.out.println("        проверка наличия элемента на форме " + Optional.ofNullable(element.getName()).orElse(element.getLocator()));
                    seleniumServise().findSingleVisibleElement(element);
                });
    }
}
