package models.testcase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "testCase")
public class TestCaseName {
    @JacksonXmlProperty(localName = "testCaseName")
    private String testCaseName;
    @JacksonXmlProperty(localName = "webPackageName")
    private String webPackageName;

    public String getTestCaseName() {
        return testCaseName;
    }

    @Override
    public String toString() {
        return "TestCaseName{" +
                "testCaseName='" + testCaseName + '\'' +
                ", webPackageName='" + webPackageName + '\'' +
                '}';
    }
}
