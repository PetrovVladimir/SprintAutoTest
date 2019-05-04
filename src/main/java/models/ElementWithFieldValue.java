package models;

import models.testcase.Value;
import models.webpackage.Element;

public class ElementWithFieldValue {
    private Element element;
    private Value value;

    public ElementWithFieldValue(Element element, Value value) {
        this.element = element;
        this.value = value;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
