package models.testcase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "testCase")
public class TestCaseModel {
    @JacksonXmlProperty(localName = "testCaseName")
    private String testCaseName;
    @JacksonXmlProperty(localName = "webPackageName")
    private String webPackageName;
    @JacksonXmlElementWrapper(localName = "actions")
    private List<Action> actions;

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getWebPackageName() {
        return webPackageName;
    }

    public List<Action> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        return "TestCaseModel{" +
                "testCaseName='" + testCaseName + '\'' +
                ", webPackageName='" + webPackageName + '\'' +
                ", actions=" + actions +
                '}';
    }
}
