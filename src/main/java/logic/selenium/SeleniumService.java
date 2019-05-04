package logic.selenium;

import models.ElementWithStringValue;
import models.webpackage.Element;
import org.openqa.selenium.WebElement;


public interface SeleniumService {
    void initialization(boolean webDriverWait);

    void nacigateTo(String url);
    void refreshPage();
    boolean checkElementNotPresent(Element element);
    WebElement findSingleVisibleElement(Element element);
    WebElement findSingleElementInDOM(Element element);
    void enterSingleValuesToWebField(ElementWithStringValue element);
    void click(Element element);
    String getInputValue(Element element);
    Object jsReturnsValue(String jsFunction);
    //Actions actions
    void doubleClick(Element element);
    void moveMouseToElement(Element element);
    void pressKey(CharSequence charSequence);
    void getScreenShot(String storage);
}
