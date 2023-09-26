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
    ThreadLocal<SoftAssertion> softAssertionMap= new ThreadLocal<>();
    BrowserActions browserActions = new BrowserActions();
    @Parameters("browser")
    @BeforeMethod
    public void setup(@Optional("CHROME") String browser, Object[] testData, ITestContext ctx, Method method) {

            if (testData.length > 0) {
                if (testData[0] instanceof CredentialsDTO) {
                    testName.set(method.getName() + "_" + ((CredentialsDTO) testData[0]).getUsername());
                } else if (testData[0] instanceof ShoppingItemDTO) {
                    testName.set(method.getName() + "_" + ((ShoppingItemDTO) testData[0]).getName());

                }
                ctx.setAttribute("testName", testName.get());
            } else
                ctx.setAttribute("testName", method.getName());

        browserActions.createBrowserSession(BrowserActions.BrowserTypes.valueOf(browser),Boolean.valueOf(System.getProperty("useDocker")));

            softAssertionMap.set(new SoftAssertion());

    }

    @AfterMethod
    public void tearDown(final ITestContext testContext){
        browserActions.closeBrowser();

    }


    @Override
    public String getTestName() {
        return testName.get();
    }


}
