package models;

import models.webpackage.Element;

public class ElementWithStringValue {
    private Element element;
    private String value;

    public ElementWithStringValue(Element element, String value) {
        this.element = element;
        this.value = value;
    }

    public Element getElement() {
        return element;
    }

    public String getValue() {
        return value;
    }
}
