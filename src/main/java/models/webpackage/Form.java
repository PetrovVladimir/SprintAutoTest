package models.webpackage;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JacksonXmlRootElement(localName = "form")
public class Form implements Comparable<Form> {

    @JacksonXmlProperty(localName = "formName")
    private String formName;
    @JacksonXmlElementWrapper(localName = "elements")
    private List<Element> elements;

    public Form() {
    }

    public Form(String formName) {
        this.formName = formName;
    }

    public String getFormName() {
        return formName;
    }

    public List<Element> getElements() {
        return elements;
    }

    public Element getElementByName(String elementName) {
        int pos = Collections.binarySearch(getElements(), new Element(elementName));
        return pos >= 0 ? getElements().get(pos) : null;

    }

    @Override
    public String toString() {
        return "Form{" +
                "formName='" + formName + '\'' +
                ", elements=" + elements +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Form form = (Form) o;
        return Objects.equals(formName, form.formName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formName);
    }

    @Override
    public int compareTo(Form o) {
        return formName.compareTo(o.getFormName());
    }
}
