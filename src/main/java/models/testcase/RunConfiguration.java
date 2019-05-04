package models.testcase;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Collections;
import java.util.List;

@JacksonXmlRootElement(localName = "runConfiguration")
public class RunConfiguration {
    @JacksonXmlProperty(localName = "formName")
    private String formName;
    @JacksonXmlProperty(localName = "openValidation", isAttribute = true)
    private boolean openValidation;
    @JacksonXmlProperty(localName = "closeValidation", isAttribute = true)
    private boolean closeValidation;
    @JacksonXmlProperty(localName = "repeatsOnError")
    private int repeatsOnError;
    @JacksonXmlElementWrapper(localName = "events")
    private List<Event> events;
    @JacksonXmlElementWrapper(localName = "exceptionBlock")
    private List<Event> exceptionBlock;

    public String getFormName() {
        return formName;
    }

    public boolean isOpenValidation() {
        return openValidation;
    }

    public void setOpenValidation(byte openValidation) {
        this.openValidation = (openValidation == 1);
    }

    public boolean isCloseValidation() {
        return closeValidation;
    }

    public void setCloseValidation(byte closeValidation) {
        this.closeValidation = (closeValidation == 1);
    }

    public int getRepeatsOnError() {
        return repeatsOnError;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Event> getExceptionBlock() {
        return exceptionBlock;
    }

    @Override
    public String toString() {
        return "RunConfiguration{" +
                "formName='" + formName + '\'' +
                ", openValidation=" + openValidation +
                ", closeValidation=" + closeValidation +
                ", repeatsOnError=" + repeatsOnError +
                ", events=" + events +
                ", exceptionBlock=" + exceptionBlock +
                '}';
    }
}
