package POM;

import Actions.UI.WebUIActions;
import DTOs.ElementDTO;

import static Wrappers.LoggingHandling.logger;

public class LoginPage {

    String loginPageUrl = "https://qa-challenge.codesubmit.io/";

    ElementDTO usernameTextField = new ElementDTO("user-name", WebUIActions.Locators.id),
                passwordTextField = new ElementDTO("password", WebUIActions.Locators.id),
                loginBtn= new ElementDTO("login-button", WebUIActions.Locators.id),
                errorMessage= new ElementDTO("//h3[@data-test='error']", WebUIActions.Locators.XPath);


    WebUIActions uiActions;
    public LoginPage(String key){

        uiActions = new WebUIActions(key);
    }

    public LoginPage(){
        uiActions = new WebUIActions();
    }

    public void navigateToLoginPage(Boolean... elementAssertion){
        if(elementAssertion.length>0 && elementAssertion[0]==true ) {
            uiActions.navigateToPage(loginPageUrl, usernameTextField);
        }
        else{
            uiActions.navigateToPage(loginPageUrl);
        }
    }

    public boolean login (String username,String password){
        logger.info(String.format("Login in using credentials of username: %s and password: %s",username,password));
        uiActions.setText(this.usernameTextField,username,true,true);
        uiActions.setText(passwordTextField,password,true,true);
        HomePage home = new HomePage();
        try {
            uiActions.clickOn(loginBtn, true,
                    home.productsSpan);
            logger.info("Log in succeeded");
            return true;
        }
        catch (Exception e){
            logger.severe("log in failed because of: "+e.getMessage());
            return  false;
        }
    }

    public String getErrorMessage(){
       return uiActions.getText(errorMessage);
    }


}
