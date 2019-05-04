package models.webpackage;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "webPackage")
public final class WebPackage {

    @JacksonXmlProperty(localName = "webPackageName")
    private String webPackageName;
    @JacksonXmlElementWrapper(localName = "forms")
    private List<Form> forms;

    public String getWebPackageName() {
        return webPackageName;
    }

    public List<Form> getForms() {
        return forms;
    }

    @Override
    public String toString() {
        return "WebPackage{" +
                "webPackageName='" + webPackageName + '\'' +
                ", forms=" + forms +
                '}';
    }
}
