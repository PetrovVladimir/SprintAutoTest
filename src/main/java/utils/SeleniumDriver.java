package utils;

import application.ApplicationConfig;
import logic.selenium.SeleniumService;
import logic.selenium.SeleniumWrapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class SeleniumDriver {
    private static SeleniumDriver ourInstance = new SeleniumDriver();

    public WebDriver driver;
    public WebDriverWait webDriverWait;
    public WebDriverWait webDriverWaitLow;
    public JavascriptExecutor jse;
    public Actions actions;
    public SeleniumService seleniumService;

    public static SeleniumDriver webDriver() {
        return ourInstance;
    }

    private SeleniumDriver() {
        System.setProperty("webdriver.gecko.driver", ApplicationConfig.getGeckodriverPath()); //задаем значение свойств для гекодрайвера
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null"); //убираем эти мерзкие логи

        driver = new FirefoxDriver(); //инициализируем наш драйвер
        driver.manage().timeouts().implicitlyWait(250, TimeUnit.MILLISECONDS); //задаем неявные ожидания - сколько будет искать появление элементов в коде страницы
        driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(25, TimeUnit.SECONDS); //задаем неявные ожидания обработки скриптов
        webDriverWait = new WebDriverWait(driver, ApplicationConfig.getDefaultTimeOutsForWebDriverWait()); //задаем значение для явных ожиданий
        webDriverWaitLow = new WebDriverWait(driver, ApplicationConfig.getLowTimeOutsForWebDriverWait());
        jse = (JavascriptExecutor) driver;
        actions = new Actions(driver);

        seleniumService = new SeleniumWrapper();
    }
}
