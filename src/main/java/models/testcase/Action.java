package models.testcase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "action")
public class Action implements Comparable<Action>{
    @JacksonXmlProperty(localName = "name")
    private String name;
    @JacksonXmlProperty(localName = "orderNumber")
    private int orderNumber;
    @JacksonXmlElementWrapper(localName = "runConfiguration")
    private RunConfiguration runConfigurations;

    public Action() {
    }

    public Action(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public RunConfiguration getRunConfigurations() {
        return runConfigurations;
    }

    @Override
    public String toString() {
        return "Action{" +
                "name='" + name + '\'' +
                ", orderNumber=" + orderNumber +
                ", runConfigurations=" + runConfigurations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return orderNumber == action.orderNumber;
    }

    @Override
    public int hashCode() {
        return orderNumber;
    }

    @Override
    public int compareTo(Action o) {
        return Integer.compare(orderNumber, o.getOrderNumber());
    }
}
