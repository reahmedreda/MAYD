package POM;

import Actions.UI.WebUIActions;
import DTOs.ElementDTO;

public class CartPage {
    String url = "https://qa-challenge.codesubmit.io/cart.html";
    WebUIActions uiActions;

    ElementDTO cartItemsList = new ElementDTO("cart_item", WebUIActions.Locators.className),
                cartItemName = new ElementDTO("//div[@class='inventory_item_name' and text()='%s']", WebUIActions.Locators.XPath),
                cartItemPriceElement= new ElementDTO("//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='cart_item_label']//div[@class='inventory_item_price']", WebUIActions.Locators.XPath),
                removeCartItemBtn = new ElementDTO("//div[@class='inventory_item_name' and text()='%s']//ancestor::div[@class='cart_item_label']//button[text()='Remove']", WebUIActions.Locators.XPath),
                cartTitleSpan = new ElementDTO("//span[@class='title' and text()='Your Cart']", WebUIActions.Locators.XPath),
                getRemoveCartItemsBtns = new ElementDTO("//button[contains(@class,'btn') and text()='Remove']",WebUIActions.Locators.XPath),
                checkoutBtn = new ElementDTO("checkout", WebUIActions.Locators.id);

    public CartPage(String key){
        uiActions = new WebUIActions(key);
    }

    public CartPage(){
        uiActions = new WebUIActions();
    }

    public void navigateToCart(Boolean... elementAssertion) {
        if(elementAssertion.length>0 && elementAssertion[0]==true ) {
            uiActions.navigateToPage(url, cartTitleSpan);
        }
        else{
            uiActions.navigateToPage(url);
        }
    }
    public int getCartItemsCount(){
        return uiActions.getMatchingElementsCount(cartItemsList);
    }

    public float getItemPrice(String item){
        String price = uiActions.getText(
                        new ElementDTO(String.format(cartItemPriceElement.selector,item),
                        cartItemPriceElement.locator));
        return Float.valueOf(price.replaceAll("[^\\d.]", ""));

    }

    public boolean removeItemFromCart(String itemName) {
        int count = getCartItemsCount();
        if(count>0) {
            try {
                uiActions.clickOn(new ElementDTO(
                        String.format(removeCartItemBtn.selector, itemName),
                        removeCartItemBtn.locator));
            }
            catch(Exception e){
                return false;
            }
            if (getCartItemsCount() == count - 1 &&
                    uiActions.isElementExist(new ElementDTO(
                            String.format(cartItemName.selector, itemName)
                            , cartItemName.locator)) == false) {
                return true;
            }

            return false;
        }
        else{
            return true;
        }


    }
    public boolean removeAllItemsFromCart() {
        int count = getCartItemsCount();
        if(count>0) {
            uiActions.clickOnListOfButtons(getRemoveCartItemsBtns);
            if (getCartItemsCount() == 0) {
                return true;
            }

            return false;
        }
        else {
            return true;
        }
    }

    public boolean isItemInCart(String itemName){
        return uiActions.isElementExist(new ElementDTO(
                String.format(cartItemName.selector, itemName)
                , cartItemName.locator));
    }

    public boolean checkout(){
        try {
            uiActions.clickOn(new ElementDTO(checkoutBtn.selector,
                    checkoutBtn.locator),true,new CheckoutPage().continueBtn);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
