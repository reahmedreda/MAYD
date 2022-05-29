package Actions.UI;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BrowserActions {

    public static Map<String,RemoteWebDriver> mapper = new HashMap<>();
    public static WebDriver driver;
    public static RemoteWebDriver remoteWebDriver;

    static boolean chromeDriverSetUp = false,
                    firefoxDriverSetUp = false,
                    useSeleniumGrid = false;


    public static void addWebDriverToMapOfDrivers(String browser, String uniqueKey) {
        try{
            try{
                if(Boolean.valueOf(System.getProperty("useDocker"))){
                    RemoteWebDriver d = addRemoteDriverToMap(browser);
                    if(!mapper.containsKey(uniqueKey)) {
                        mapper.put(uniqueKey, d);
                        return;
                    }
                    else{
                        mapper.replace(uniqueKey,d);
                        return;
                    }
                }
            } catch (Exception e){}

            if(browser.toLowerCase().equals(Browsers.chrome.toString().toLowerCase())) {
                if(!chromeDriverSetUp){
                    WebDriverManager.chromedriver().setup();
                    BrowserActions.chromeDriverSetUp = true;
                }
                //adding a new driver to the map and link it with a unique key
                if(!mapper.containsKey(uniqueKey)) {
                    mapper.put(uniqueKey, new ChromeDriver());
                }
                else{
                    mapper.replace(uniqueKey,new ChromeDriver());
                }
            }
            else if(browser.toLowerCase().equals(Browsers.chromeHeadless.toString().toLowerCase())) {
                if(!chromeDriverSetUp){
                    WebDriverManager.chromedriver().setup();
                    BrowserActions.chromeDriverSetUp = true;
                }
                //adding a new driver to the map and link it with a unique key
                if(!mapper.containsKey(uniqueKey)) {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless");
                    mapper.put(uniqueKey, new ChromeDriver(options));
                }
                else{
                    mapper.replace(uniqueKey,new ChromeDriver());
                }
            }
            else if(browser.toLowerCase().equals(Browsers.firefox.toString().toLowerCase())) {
                if(!firefoxDriverSetUp){
                    WebDriverManager.firefoxdriver().setup();
                    BrowserActions.firefoxDriverSetUp = true;
                }
                if(!mapper.containsKey(uniqueKey)) {
                    mapper.put(uniqueKey, new FirefoxDriver());
                }
                else{
                    mapper.replace(uniqueKey,new FirefoxDriver());
                }
            }
        }

        catch(Exception e){
            //Handle this exception instead of returning null

        }
    }
    public static void closeDriverAndRemoveFromMap(String key){
        if(mapper.containsKey(key)) {
            try {
                mapper.get(key).quit();
            }
            catch(Exception e){

            }
            mapper.remove(key);
        }
    }

    public static void closeAllDriversFromMap(){
        Set<String> keys = mapper.keySet();
        if(keys!=null && keys.size()>0) {
            for (String key:keys) {
                    try {
                        mapper.get(key).quit();
                    } catch (Exception e) {

                    }
                    mapper.remove(key);
                }
            }
        }

    public static void initializeWebDriver(String browser){
        try{
            if(browser.toLowerCase().equals(Browsers.chrome.toString())) {
                driver = new ChromeDriver();

            }
            else if(browser.toLowerCase().equals(Browsers.firefox.toString())) {
                driver = new FirefoxDriver();
            }
        }

        catch(Exception e){
            //Handle this exception instead of returning null

        }
    }
    public static void closeStaticWebDriver(){
        driver.close();
    }

    public static RemoteWebDriver addRemoteDriverToMap(String browser) throws MalformedURLException {
        DesiredCapabilities cap=new DesiredCapabilities();

        if(browser.toLowerCase().equals(Browsers.chrome.toString().toLowerCase())){
            cap = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            cap.merge(options);
        }
        else if(browser.toLowerCase().equals(Browsers.firefox.toString().toLowerCase())){
            cap = DesiredCapabilities.firefox();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");

            cap.merge(options);
        }
        else{
            Assert.fail("Wrong Browser");
        }
        cap.setPlatform(Platform.LINUX);
        cap.setVersion("");

        remoteWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
        return remoteWebDriver;
    }
    public static void closeRemoteDriver(){
        remoteWebDriver.close();
        useSeleniumGrid = false;
    }




    enum Browsers{
        chrome,
        chromeHeadless,
        firefox
    }


}
