package models.testcase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "field")
public class Field {
    @JacksonXmlProperty(localName = "element")
    private String element;
    @JacksonXmlElementWrapper(localName = "value", useWrapping = false)
    private Value value;
    @JacksonXmlProperty(localName = "xpath")
    private String xpath;

    public String getElement() {
        return element;
    }

    public String getXpath() {
        return xpath;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Field{" +
                "element='" + element + '\'' +
                ", value=" + value +
                ", xpath='" + xpath + '\'' +
                '}';
    }
}
