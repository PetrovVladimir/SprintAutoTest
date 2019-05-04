package models.webpackage;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Comparator;
import java.util.Objects;

@JacksonXmlRootElement(localName = "element")
public class Element implements Comparable<Element> {
    @JacksonXmlProperty(localName = "name")
    private String name;
    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private byte type;
    @JacksonXmlElementWrapper(localName = "locator", useWrapping = false)
    private Locator locator;
    @JacksonXmlProperty(localName = "alwaysVisible", isAttribute = true)
    private boolean alwaysVisible;

    public Element() {
    }

    public Element(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public byte getType() {
        return type;
    }

    public byte getLocatorType() {
        return locator.getType();
    }

    public String getLocator() {
        return locator.getLocator();
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    public boolean getAlwaysVisible() {
        return alwaysVisible;
    }

    public void setAlwaysVisible(byte alwaysVisible) {
        this.alwaysVisible = (alwaysVisible == 1);
    }

    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", locator='" + locator + '\'' +
                ", alwaysVisible=" + alwaysVisible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return type == element.type &&
                Objects.equals(name, element.name) &&
                Objects.equals(locator, element.locator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, locator);
    }

    @Override
    public int compareTo(Element o) {
        Comparator<String> nullSafeStringComparator = Comparator
                .nullsFirst(String::compareTo);
        return nullSafeStringComparator.compare(this.name, o.getName());
    }

}
