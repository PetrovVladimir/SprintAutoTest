package models.testcase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "event")
public class Event implements Comparable<Event>{
    @JacksonXmlProperty(localName = "orderNumber")
    private int orderNumber;
    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private String type;
    @JacksonXmlProperty(localName = "invertResult", isAttribute = true)
    private boolean invertResult;
    @JacksonXmlProperty(localName = "hasExceptionBlock", isAttribute = true)
    private boolean hasExceptionBlock;
    @JacksonXmlProperty(localName = "url")
    private String url;
    @JacksonXmlProperty(localName = "elementName")
    private String elementName;
    @JacksonXmlProperty(localName = "utilityValue")
    private String utilityValue;
    @JacksonXmlElementWrapper(localName = "fields")
    private List<Field> fields;
    @JacksonXmlProperty(localName = "dbRequest")
    private String dbRequest;

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getType() {
        return type;
    }

    public boolean isInvertResult() {
        return invertResult;
    }

    public void setInvertResult(byte invertResult) {
        this.invertResult = (invertResult == 1);
    }

    public boolean isHasExceptionBlock() {
        return hasExceptionBlock;
    }

    public void setHasExceptionBlock(byte hasExceptionBlock) {
        this.hasExceptionBlock = (hasExceptionBlock == 1);
    }

    public String getUrl() {
        return url;
    }

    public String getElementName() {
        return elementName;
    }

    public String getUtilityValue() {
        return utilityValue;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getDbRequest() {
        return dbRequest;
    }

    @Override
    public String toString() {
        return "Event{" +
                "orderNumber=" + orderNumber +
                ", type='" + type + '\'' +
                ", invertResult=" + invertResult +
                ", hasExceptionBlock=" + hasExceptionBlock +
                ", url='" + url + '\'' +
                ", elementName='" + elementName + '\'' +
                ", utilityValue='" + utilityValue + '\'' +
                ", fields=" + fields +
                ", dbRequest='" + dbRequest + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return orderNumber == event.orderNumber;
    }

    @Override
    public int hashCode() {
        return orderNumber;
    }

    @Override
    public int compareTo(Event o) {
        return Integer.compare(orderNumber, o.orderNumber);
    }
}
