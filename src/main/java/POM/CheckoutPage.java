package POM;

import Actions.UI.WebUIActions;
import DTOs.ElementDTO;
import org.openqa.selenium.WebElement;

public class CheckoutPage {
    ElementDTO firstNameInput = new ElementDTO("first-name", WebUIActions.Locators.id),
                lastNameInput = new ElementDTO("last-name", WebUIActions.Locators.id),
                zipCodeInput = new ElementDTO("postal-code", WebUIActions.Locators.id),
               continueBtn = new ElementDTO("continue", WebUIActions.Locators.id),
                finishBtn = new ElementDTO("finish", WebUIActions.Locators.id),
                thankYouForYourOrderHeader = new ElementDTO("//h2[text()='THANK YOU FOR YOUR ORDER']", WebUIActions.Locators.XPath),
                backToProductsBtn = new ElementDTO("back-to-products", WebUIActions.Locators.id);

    WebUIActions uiActions;


    public CheckoutPage(){
        uiActions = new WebUIActions();
    }

    public boolean completeCheckout(String firstName,String lastName,String postalCode){
        uiActions.setText(firstNameInput,firstName,true,false);
        uiActions.setText(lastNameInput,lastName,true,false);
        uiActions.setText(zipCodeInput,postalCode,true,false);

        try {
            uiActions.clickOn(continueBtn,true,finishBtn);
            uiActions.clickOn(finishBtn,true,thankYouForYourOrderHeader);
            uiActions.clickOn(backToProductsBtn,true,new HomePage().productsSpan);
            if(!uiActions.isElementExist(new HomePage().shoppingCartCounter)) {
                return true;
            }
            return false;
        }
        catch(Exception e){
            return false;
        }
    }
}
