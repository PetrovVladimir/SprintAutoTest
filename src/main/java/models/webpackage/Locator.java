package models.webpackage;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;


public class Locator {
    @JacksonXmlText
    private String locator;
    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private byte type = 2;

    public Locator() {
    }

    public Locator(String locator, byte type) {
        this.locator = locator;
        this.type = type;
    }

    byte getType() {
        return type;
    }

    public String getLocator() {
        return locator;
    }

    @Override
    public String toString() {
        return "Locator{" +
                "locator='" + locator + '\'' +
                ", type=" + type +
                '}';
    }
}
