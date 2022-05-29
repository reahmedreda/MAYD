package POM;

import Actions.UI.WebUIActions;
import DTOs.ElementDTO;
import DTOs.ShoppingItemDTO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class HomePage {
    String homepageURL = "https://qa-challenge.codesubmit.io/inventory.html";
    WebUIActions uiActions;

    ElementDTO productsSpan = new ElementDTO("//span[@class='title' and text()='Products']", WebUIActions.Locators.XPath),
            shoppingCartCounter = new ElementDTO("shopping_cart_badge", WebUIActions.Locators.className),
            itemName= new ElementDTO("//div[@class='inventory_item_name' and text()='%s']", WebUIActions.Locators.XPath),
            itemPrice= new ElementDTO("//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='inventory_item_description']//div[@class='inventory_item_price']", WebUIActions.Locators.XPath),
            itemDesc = new ElementDTO("//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='inventory_item_label']//div[@class='inventory_item_desc']", WebUIActions.Locators.XPath),
            itemAddToCartBtn = new ElementDTO("//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='inventory_item_description']//button[text()='Add to cart']", WebUIActions.Locators.XPath),
            itemRemoveFromCartBtn = new ElementDTO("//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='inventory_item_description']//button[text()='Remove']", WebUIActions.Locators.XPath),
            itemImage = new ElementDTO("//div[@class='inventory_item_name' and text()='%s']/ancestor::div[@class='inventory_item']//img[@class='inventory_item_img']", WebUIActions.Locators.XPath);
    ;


    public HomePage(){
        uiActions = new WebUIActions();
    }

    public HomePage(String key){
        uiActions = new WebUIActions(key);
    }

    public void navigateToHome(Boolean... elementAssertion){
        if(elementAssertion.length>0 && elementAssertion[0]==true ) {
            uiActions.navigateToPage(homepageURL, productsSpan);
        }
        else{
            uiActions.navigateToPage(homepageURL);
        }
    }

    public int getShoppingCartCount(){
        if(checkIfShoppingCartCounterIsInvisible()) {
            return Integer.parseInt(uiActions.getText(shoppingCartCounter));
        }
        else return 0;
    }
    public boolean checkIfShoppingCartCounterIsInvisible(){
        return uiActions.isElementExist(shoppingCartCounter);
    }

    public boolean addItemToCart(String itemName){
        try{
            int count = getShoppingCartCount();
            uiActions.clickOn(
                    new ElementDTO(String.format(itemAddToCartBtn.selector,itemName),itemAddToCartBtn.locator),
                    true,
                    new ElementDTO(String.format(itemRemoveFromCartBtn.selector,itemName),itemRemoveFromCartBtn.locator));
            if(getShoppingCartCount() == count+1) {
                return true;
            }
            else{
                return false;
            }

        }
        catch (Exception e){
            return false;
        }
    }

    public boolean removeItem(String itemName){
        try{
            int count = getShoppingCartCount();
            uiActions.clickOn(
                    new ElementDTO(String.format(itemRemoveFromCartBtn.selector,itemName),itemRemoveFromCartBtn.locator),
                    true,
                    new ElementDTO(String.format(itemAddToCartBtn.selector,itemName),itemAddToCartBtn.locator));
            if(getShoppingCartCount() == count-1) {
                return true;
            }
            else{
                return false;
            }

        }
        catch (Exception e){
            return false;
        }
    }
    public boolean downloadItemImage(String itemName,String folder,String path){
        String src = uiActions.getAttribute(
                new ElementDTO(String.format(itemImage.selector,itemName),itemImage.locator)
                ,"src");

        try {
            File directory = new File(folder);
            if (! directory.exists()){
                directory.mkdir();
            }
            String fullPath=folder+"/"+path+".png";
            File file = new File(fullPath);
            Files.deleteIfExists(file.toPath());


            URL url = new URL(src);
            BufferedImage bufImgOne = ImageIO.read(url);
            ImageIO.write(bufImgOne, "png", new File(fullPath));
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

//    public boolean checkItemExists(ShoppingItemDTO item){
//
//    }






}
