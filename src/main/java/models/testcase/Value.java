package models.testcase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Value {
    @JacksonXmlText
    private String value;
    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private byte type = 1;

    public String getValue() {
        return value;
    }

    public byte getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Value{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
