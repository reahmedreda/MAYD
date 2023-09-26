package POM;

import Actions.UI.WebUIActions;
import DTOs.ElementDTO;

public class BurgerMenu {
    ElementDTO burgerMenuIcon = new ElementDTO("react-burger-menu-btn", WebUIActions.Locators.id),
                logout = new ElementDTO("logout_sidebar_link", WebUIActions.Locators.id);

    WebUIActions uiActions;
    public BurgerMenu(){
        uiActions = new WebUIActions();
    }

    public boolean logout(){
        try {
            uiActions.clickOn(burgerMenuIcon, true, logout);
            uiActions.clickOn(logout,true,new LoginPage().usernameTextField);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
