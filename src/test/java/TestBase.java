import Actions.UI.BrowserActions;
import DTOs.CredentialsDTO;
import DTOs.ShoppingItemDTO;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.*;
import utils.SoftAssertion;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class TestBase implements ITest {

    private ThreadLocal<String> testName = new ThreadLocal<>();
    Map<String, SoftAssertion> softAssertionMap= new HashMap<>();

    @Parameters("browser")
    @BeforeMethod
    public void setup(String browser,Object[] testData, ITestContext ctx,Method method) {
        String uniqueKey = String.valueOf(Thread.currentThread().getId());

            if (testData.length > 0) {
                if (testData[0] instanceof CredentialsDTO) {
                    testName.set(method.getName() + "_" + ((CredentialsDTO) testData[0]).getUsername());
                } else if (testData[0] instanceof ShoppingItemDTO) {
                    testName.set(method.getName() + "_" + ((ShoppingItemDTO) testData[0]).getName());

                }
                ctx.setAttribute("testName", testName.get());
            } else
                ctx.setAttribute("testName", method.getName());
             System.out.println(uniqueKey);
            BrowserActions.addWebDriverToMapOfDrivers(browser, uniqueKey);

        if(softAssertionMap.containsKey(uniqueKey)){
            softAssertionMap.replace(uniqueKey,new SoftAssertion());
        }
        else {
            softAssertionMap.put(uniqueKey, new SoftAssertion());
        }
    }

    @AfterMethod
    public void tearDown(final ITestContext testContext){
        boolean docker= false;
        String uniqueKey = String.valueOf(Thread.currentThread().getId());
        BrowserActions.closeDriverAndRemoveFromMap(uniqueKey);

    }



    @AfterSuite
    public void afterSuite(){
        try {
            BrowserActions.closeAllDriversFromMap();
        }
        catch (Exception e){}
    }

    @Override
    public String getTestName() {
        return testName.get();
    }


}
