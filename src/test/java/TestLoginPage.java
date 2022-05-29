import DTOs.CredentialsDTO;
import POM.LoginPage;
import Wrappers.ConfigPropertiesFileHandler;
import Wrappers.JsonFileParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestLoginPage extends TestBase{

    JsonFileParser jsonFileParser;

    @BeforeClass
    public void loginSetup(){
        jsonFileParser= new JsonFileParser(
                ConfigPropertiesFileHandler.testDataDirPath
                        +"loginCredentialsUseCases.json");
    }

    //This test is an example of a functional testing for a single feature "Log in"
    //The test will be ran versus multiple credentials and the output will be
    //a list of separate test results for each credential
    @Test(dataProvider = "getCredentialsTestData")
    public void testLoginWithAllUsers(CredentialsDTO credentials){
        String uniqueKey = String.valueOf(Thread.currentThread().getId());
        LoginPage loginPage =new LoginPage(uniqueKey);
        loginPage.navigateToLoginPage();
        boolean loginSucceeded = loginPage.login(credentials.getUsername(),credentials.getPassword());
        Assert.assertEquals(loginSucceeded,credentials.isExpectedToLogin(),"asserting login status");
        if(!loginSucceeded){
            Assert.assertEquals(loginPage.getErrorMessage(),credentials.getError(),"asserting error message");
        }
    }

    @DataProvider(parallel = true)
    public Object[] getCredentialsTestData(){
        JSONArray credentials = jsonFileParser.getJsonArrayOfNode("credentials");
        Object[] data = new Object[credentials.length()];
        for(int i=0;i<credentials.length();i++){
            JSONObject currentCredentialsJsonObject  = credentials.getJSONObject(i);
            CredentialsDTO currentCredentials = new CredentialsDTO();
            currentCredentials.setUsername(currentCredentialsJsonObject.getString(CredentialsDTO.propertiesNames.username.value));
            currentCredentials.setPassword(currentCredentialsJsonObject.getString(CredentialsDTO.propertiesNames.password.value));
            currentCredentials.setExpectedToLogin(currentCredentialsJsonObject.getBoolean(CredentialsDTO.propertiesNames.expectedToLogin.value));
            currentCredentials.setError(currentCredentialsJsonObject.getString(CredentialsDTO.propertiesNames.expectedErrorMessage.value));
            data[i]= currentCredentials;
        }

        return data;
    }
}

