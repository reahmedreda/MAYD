package Actions.UI;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BrowserActions {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();


    public WebDriver getBrowserSession() {
        return driverThreadLocal.get();
    }


    public void createBrowserSession(BrowserTypes driverType, Boolean... useRemote) {
        WebDriver driver;
        boolean remote = useRemote.length > 0 ? useRemote[0]:false;
        if (remote) {
            try {
                driver = createRemoteBrowserSession(driverType);
            } catch (MalformedURLException e) {
                System.err.println("Selenium Grid URL is invalid. Falling back to local browser session.");
                driver = createLocalBrowserSession(driverType);
            }
        } else {
            driver = createLocalBrowserSession(driverType);
        }
        driverThreadLocal.set(driver);
    }


    public void closeBrowser() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    private WebDriver createRemoteBrowserSession(BrowserTypes driverType) throws MalformedURLException {
        return new RemoteWebDriver(getGridURL(), getOptions(driverType));
    }

    private WebDriver createLocalBrowserSession(BrowserTypes driverType) {
        switch (driverType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            case CHROME_HEADLESS:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                return  new ChromeDriver(chromeOptions);
            case FIREFOX:
                return new FirefoxDriver();
            case FIREFOX_HEADLESS:
                FirefoxOptions headlessFirefoxOptions = new FirefoxOptions();
                headlessFirefoxOptions.setHeadless(true);
                return new FirefoxDriver(headlessFirefoxOptions);
            // Add more cases for other driver types if needed
            default:
                throw new IllegalArgumentException("Invalid driver type: " + driverType);
        }
    }

    private URL getGridURL() throws MalformedURLException {
        return new URL("http://localhost:4444/wd/hub");
    }

    private DesiredCapabilities getOptions(BrowserTypes driverType) {
        switch (driverType) {
            case CHROME:
                return DesiredCapabilities.chrome();
            case CHROME_HEADLESS:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                return DesiredCapabilities.chrome().merge(chromeOptions);
            case FIREFOX:
                return DesiredCapabilities.firefox();
            case FIREFOX_HEADLESS:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(true);
                return DesiredCapabilities.firefox().merge(firefoxOptions);
            // Add more cases for other driver types if needed
            default:
                throw new IllegalArgumentException("Invalid driver type: " + driverType);
        }
    }


    public void navigateTo(String url) {
        WebDriver driver = driverThreadLocal.get();
        driver.get(url);
    }


    public String getPageTitle() {
        WebDriver driver = driverThreadLocal.get();
        return driver.getTitle();
    }


    public String getCurrentURL() {
        WebDriver driver = driverThreadLocal.get();
        return driver.getCurrentUrl();
    }


    public void maximizeWindow() {
        WebDriver driver = driverThreadLocal.get();
        driver.manage().window().maximize();
    }


    public void navigateBack() {
        WebDriver driver = driverThreadLocal.get();
        driver.navigate().back();
    }


    public void navigateForward() {
        WebDriver driver = driverThreadLocal.get();
        driver.navigate().forward();
    }


    public void refreshPage() {
        WebDriver driver = driverThreadLocal.get();
        driver.navigate().refresh();
    }


    public void takeScreenshot(String filePath) {
        WebDriver driver = driverThreadLocal.get();
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotFile, new File(filePath));
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }


    public void waitForPageLoad() {
        WebDriver driver = driverThreadLocal.get();
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String pageLoadStatus;
        do {
            pageLoadStatus = (String) jsExecutor.executeScript("return document.readyState");
        } while (!pageLoadStatus.equals("complete"));
    }


    public void switchToFrame(String frameName) {
        WebDriver driver = driverThreadLocal.get();
        driver.switchTo().frame(frameName);
    }


    public void switchToWindow(String windowHandle) {
        WebDriver driver = driverThreadLocal.get();
        driver.switchTo().window(windowHandle);
    }


    public void executeJavaScript(String script) {
        WebDriver driver = driverThreadLocal.get();
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(script);
    }

    public enum BrowserTypes {
        CHROME,
        CHROME_HEADLESS,
        FIREFOX,
        FIREFOX_HEADLESS
        // Add more driver types if needed
    }

}
