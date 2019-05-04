package logic.selenium;

import models.ElementWithStringValue;
import models.webpackage.Element;
import models.webpackage.Locator;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.SeleniumDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Function;

public class SeleniumWrapper implements SeleniumService{

    private WebDriver driver;
    private WebDriverWait webDriverWait;
    private JavascriptExecutor jse;
    private Actions actions;

    @Override
    public void initialization(boolean speed) {
        driver = SeleniumDriver.webDriver().driver;
        jse = SeleniumDriver.webDriver().jse;
        actions = SeleniumDriver.webDriver().actions;
        webDriverWait = !speed ? SeleniumDriver.webDriver().webDriverWait : SeleniumDriver.webDriver().webDriverWaitLow;
    }

    @Override
    public void nacigateTo(String url) {
        driver.navigate().to(url);
    }

    @Override
    public void refreshPage() {
        driver.navigate().refresh();
    }

    @Override
    public WebElement findSingleVisibleElement(Element element) {
        return (WebElement) getWaitExpectedConditionResult(WaitConditions.visible, element, "Указанный элемент отсутствует на странице");
    }

    @Override
    public WebElement findSingleElementInDOM(Element element) {
        return (WebElement) getWaitExpectedConditionResult(WaitConditions.exist, element, "Указанный элемент не найден в DOM страницы");
    }

    @Override
    public boolean checkElementNotPresent(Element element) {
        return (boolean) getWaitExpectedConditionResult(WaitConditions.invisible, element, "Элемент не исчес со страницы");
    }

    //вспомогательный метод для клика
    private WebElement clickable(Element element) { //метод для удобства
        return (WebElement) getWaitExpectedConditionResult(WaitConditions.clickable, element, "Указанный элемент не кликабельный");
    }

    @Override
    public void enterSingleValuesToWebField(ElementWithStringValue element) {
        WebElement webElement;
        Element tempElement;

        switch (element.getElement().getType()) {
            case (2) -> {//для простого типа input
                webElement = findSingleElementInDOM(element.getElement());//в думе, потому что заполняемые поля могут не отображаться на форме - например для ввода файлов
                webElement.clear();
                webElement.sendKeys(element.getValue());
            }
            case (3) -> {//для чекбоксов
                webElement = findSingleVisibleElement(element.getElement());
                switch (element.getValue()) {//костыль, надо бы вынести в ограничения платформы, но пока это упрощает дело
                    case ("check") -> {
                        if (!webElement.isSelected()) click(element.getElement());
                    }
                    case ("uncheck") -> {
                        if (webElement.isSelected()) click(element.getElement());
                    }
                    case ("grid_check") -> click(element.getElement());
                    case ("grid_check_double") -> doubleClick(element.getElement());
                    default ->
                        throw new NullPointerException("Неверный параметр для чекбокса " + element.getElement().toString());
                }
            }
            case (4) -> {//для выпадающего списка
            }
            case (5) -> {//для выпадающего списка srm: пишем название, дожидаемся появления значения - выбираем
                webElement = findSingleVisibleElement(element.getElement());
                webElement.clear();
                webElement.sendKeys(element.getValue());

                tempElement = new Element();
                tempElement.setLocator(new Locator(".//div/span[@title=\"" + element.getValue() + "\"]", (byte) 2));
                click(tempElement);
            }
            case (6) -> {//srm select - используется на типовых функциях типа поиска и т.п.
                click(element.getElement());
                tempElement = new Element();
                tempElement.setLocator(new Locator(".//div[@role=\"listitem\" and text()=\"" + element.getValue() + "\"]", (byte) 2));
                click(tempElement);
            }
            default ->
                throw new NullPointerException("Невозможно обработать указанный тип элемента: " + element.getElement().toString());
        }
    }

    @Override
    public void click(Element element) {
        clickTempelateExpression(element, (byte) 1);
    }

    @Override
    public void doubleClick(Element element) {
        clickTempelateExpression(element, (byte) 2);
    }

    @Override
    public String getInputValue(Element element) {
        return findSingleElementInDOM(element).getAttribute("value");
    }

    @Override
    public void moveMouseToElement(Element element) {
        actions.moveToElement(findSingleVisibleElement(element)).build().perform();
    }

    @Override
    public void pressKey(CharSequence charSequence) {
        actions.sendKeys(charSequence).build().perform();
    }

    @Override
    public Object jsReturnsValue(String jsFunction) {
        return webDriverWait.until(ExpectedConditions.jsReturnsValue(jsFunction));
    }

    @Override
    public void getScreenShot(String storage) {
        try {
            Files.copy(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE).toPath(), new File(storage).toPath());
        } catch (IOException e) {
            System.out.println("Не удалось выполнить скриншот");
            System.out.println(e.getMessage());
        }
    }

    //*********ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ*****************
    private enum WaitConditions {
        visible (ExpectedConditions::visibilityOfElementLocated),
        exist (ExpectedConditions::presenceOfElementLocated),
        invisible (ExpectedConditions::invisibilityOfElementLocated),
        clickable (ExpectedConditions::elementToBeClickable);

        private Function<By, ExpectedCondition<?>> type;

        WaitConditions (Function<By, ExpectedCondition<?>> type) {
            this.type = type;
        }
    }

    private Object getWaitExpectedConditionResult(WaitConditions condition, Element element, String exceptionText) {
        try {
            return Objects.requireNonNull(condition.type.andThen(webDriverWait::until).apply(getBy(element)));
        }catch (IllegalStateException e) {
            throw e;
        }catch (Exception e) {
            throw new NullPointerException(exceptionText + ": " + element.toString());
        }
    }

    private By getBy(Element element) {
        return switch (element.getLocatorType()) {
            case (2) -> By.xpath(element.getLocator());
            case (1) -> By.id(element.getLocator());
            default -> throw new IllegalStateException("Некорректный тип локатора для элемента " + element.toString());
        };
    }

    private void clickTempelateExpression(Element element, byte clickCounts) {
        byte i = 0;
        while (i < 5) {
            try {
                switch (clickCounts) {
                    case (1) -> clickable(element).click();
                    case (2) -> actions.doubleClick(clickable(element)).build().perform();
                }
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException | NullPointerException e) {
                i++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        throw new NullPointerException("Указанный элемент не кликабельный " + element.toString());
    }

    @Deprecated
    public Object javaScriptFunction(String value) {
        return jse.executeScript(value);
    }
}