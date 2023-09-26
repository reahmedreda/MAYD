package Actions.UI;

import DTOs.ElementDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.rmi.Remote;
import java.util.List;

import static Wrappers.LoggingHandling.logger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class WebUIActions {

    public WebDriver driver;



    public WebUIActions(){
        BrowserActions browserActions=new BrowserActions();
            this.driver = browserActions.getBrowserSession();

    }

    public void setText(ElementDTO ele, String text,boolean clear,boolean assertOnActualValue) {
        By b = returnElementLocatorBy(ele.selector,ele.locator);
        WebElement element = waitUntil(b, ExpectedConditionsEnum.presenceOfElement);
        if(element !=null) {
            try {
                if (clear) {
                    element.clear();
                }
                element.sendKeys(text);
                if(assertOnActualValue) {
                    String actualValue = (
                            element.getAttribute("value") == null) ?
                            (element.getAttribute("innerHTML") == null ? element.getText() : element.getAttribute("innerHTML"))
                            : element.getAttribute("value");

                    assertEquals(actualValue, text);
                }
            } catch (Exception e) {
                String message = String.format("Couldn't set text to element with selector:" +
                        " %s because of the exception: %s",ele.selector,e.getMessage() );
                logger.severe(message);
                Assert.fail(message);

            }
        }
        else{
            String message = String.format("Element with selector: %s is null",ele.selector);
            logger.severe(message);
            Assert.fail(message);
        }
    }
    public void clickOn(ElementDTO ele,boolean assertion,ElementDTO expectedElementOb) throws Exception {
        By b = returnElementLocatorBy(ele.selector,ele.locator);
        WebElement element = waitUntil(b, ExpectedConditionsEnum.presenceOfElement);
        if(element!=null) {
            try {
                waitUntil(b, ExpectedConditionsEnum.ElementToBeClickable);
                element.click();
            } catch (Exception e) {
                try {
                    JavascriptExecutor executor = (JavascriptExecutor) driver;
                    executor.executeScript("arguments[0].click();", element);
                } catch (Exception c) {
                    String message = String.format("Couldn't click on button with selector:" +
                            " %s because of the exception: %s",ele.selector,c.getMessage() );
                    logger.severe(message);
                    throw  new Exception(message);
                }
            }
            new WebDriverWait(driver, 10).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            if(assertion) {
                    By expectedElement = returnElementLocatorBy(expectedElementOb.selector, expectedElementOb.locator);
                    if(waitUntil(expectedElement, ExpectedConditionsEnum.ElementToBeClickable)==null){
                        logger.severe(String.format("Expected element %s to appear after click is null",expectedElementOb.selector));
                        throw new Exception("Expected element to appear after click is null");
                    };
            }
        }
        else{
            String message = String.format("Element with selector: %s is null",ele.selector);
            logger.severe(message);
            throw  new Exception(message);
        }
    }
    public void clickOn(ElementDTO ele) throws Exception {
        By b = returnElementLocatorBy(ele.selector,ele.locator);
        WebElement element = waitUntil(b, ExpectedConditionsEnum.ElementToBeClickable);
       if(element!=null) {
           try {
               element.click();
           } catch (Exception e) {
               try {
                   JavascriptExecutor executor = (JavascriptExecutor) driver;
                   executor.executeScript("arguments[0].click();", element);
               } catch (Exception c) {
                   String message = String.format("Couldn't click on button with selector:" +
                           " %s because of the exception: %s",ele.selector,c.getMessage() );
                    logger.severe(message);
                   throw new Exception(message);
               }
           }
           new WebDriverWait(driver, 10).until(
                   webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
       }
       else{
           String message = String.format("Element with selector: %s is null",ele.selector);
            logger.severe(message);
           throw  new Exception(message);
       }
     }

    public WebElement waitUntil(By b, ExpectedConditionsEnum condition) {
        try {
            WebElement element = null;
            switch (condition) {
                case presenceOfElement:

                    element = (new WebDriverWait(driver,10)).until(ExpectedConditions.presenceOfElementLocated(b));
                    return element;

                case ElementToBeClickable:
                    element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(b));
                    return element;


                default:
                    element = null;
                    Assert.fail("Wrong condition");
            }
            return element ;
        } catch (Exception e) {
            return null;
        }
    }



    public String getText(ElementDTO ele){
        try{
            By b = returnElementLocatorBy(ele.selector,ele.locator);
            WebElement element = waitUntil(b,ExpectedConditionsEnum.presenceOfElement);
            if(element!=null) {
                return element.getText();
            }
            else {
                return null;
            }

        }
        catch (Exception e){
            return null;
        }
    }

    public void navigateToPage(String url, ElementDTO ele) {
        driver.get(url);
        By b = returnElementLocatorBy(ele.selector,ele.locator);
        new WebDriverWait(driver, 10).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        WebElement element = waitUntil(b, ExpectedConditionsEnum.presenceOfElement);
        assertNotNull(element, "Navigation Failed to this Website "+url);
    }

    public void navigateToPage(String url) {
        driver.get(url);
        new WebDriverWait(driver, 10).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }


    public By returnElementLocatorBy(String selector,Locators l){
        switch (l){
            case XPath:
                return new By.ByXPath(selector);

            case id:
                return new By.ById(selector);

            case className:
                return new By.ByClassName(selector);


            case CSS:
                return new By.ByCssSelector(selector);


            default: return  null;
        }
    }

    public int getMatchingElementsCount(ElementDTO ele){
        try{
            List<WebElement> elements = driver.findElements(returnElementLocatorBy(ele.selector, ele.locator));
            if(elements !=null){
                return elements.size();
            }
        }
        catch (Exception e){
        }
        return 0;

    }
    public boolean isElementExist(ElementDTO ele){
        return (!driver.findElements(
                returnElementLocatorBy(ele.selector,ele.locator)
        ).isEmpty());
    }

    public boolean clickOnListOfButtons(ElementDTO ele){
        try {


            By b = returnElementLocatorBy(ele.selector, ele.locator);
            List<WebElement> elements = driver.findElements(b);
            if (elements != null && elements.size() > 0) {
                for (int i = 0; i < elements.size(); i++) {
                    elements.get(i).click();
                }
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public String getAttribute(ElementDTO ele,String attribute){
        if(isElementExist(ele)){
            return
                    driver.findElement(returnElementLocatorBy(ele.selector,ele.locator))
                    .getAttribute(attribute);
        }
        return null;
    }
    public enum Locators {
        XPath,
        CSS,
        id,
        className
    }

    enum ExpectedConditionsEnum{
        presenceOfElement,
        ElementToBeClickable
    }

}
