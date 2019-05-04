package logic.testcase.events;

import models.EventParams;
import models.webpackage.Element;

import java.util.List;
import java.util.Optional;

public class CheckElementsInVisibilityEvent extends EventWrapper {
    private List<Element> elements;

    public CheckElementsInVisibilityEvent(EventParams params, List<Element> elements) {
        super(params);
        this.elements = elements;
    }

    @Override
    protected void doWork(){
        elements.parallelStream()
                .forEach(element -> {
                    System.out.println("        проверка отсутствия элемента на форме " + Optional.ofNullable(element.getName()).orElse(element.getLocator()));
                    if (!seleniumServise().checkElementNotPresent(element)) {
                        throw new NullPointerException("Элемент не изчез со страницы " + element.toString());
                    }
                });
    }
}
