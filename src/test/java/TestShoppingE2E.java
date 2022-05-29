import DTOs.CredentialsDTO;
import DTOs.ShoppingItemDTO;
import POM.*;
import Wrappers.ConfigPropertiesFileHandler;
import Wrappers.JsonFileParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ImageProcessingActions;
import utils.SoftAssertion;

import java.util.List;

public class TestShoppingE2E extends TestBase{

    JsonFileParser credentialsFile,itemsFile;

    @BeforeClass
    public void beforeClass(){
        //Reading Credentials test data json file
        credentialsFile= new JsonFileParser(
                ConfigPropertiesFileHandler.testDataDirPath
                        +"loginCredentialsUseCases.json");

        //Reading items test data json file
        itemsFile= new JsonFileParser(
                ConfigPropertiesFileHandler.testDataDirPath
                        +"itemsTestData.json");
    }



    //These are full E2E test scenarios that ran multiple times over multiple data
    //Each run, uses different credentials test data and a full list of items


//------------------------------------------------------------------------------//

    //The goal of this test is to to do a happy purchasing scenario with all credentials
    @Test(dataProvider = "getShoppingItemsVsAllCredentials")
    void PurchaseAllItems(CredentialsDTO credentials, ShoppingItemDTO[] items){

        //getting the unique key|current thread to use it as
        //a key to get the browser from the hash map
        //as well as to get the soft assertion object
        String uniqueKey = String.valueOf(Thread.currentThread().getId());
        SoftAssertion softAssert = softAssertionMap.get(uniqueKey);
        LoginPage loginPage =new LoginPage(uniqueKey);
        HomePage home= new HomePage(uniqueKey);
        CartPage cartPage = new CartPage(uniqueKey);

        //log in
        loginPage.navigateToLoginPage(true);
        boolean loginSucceeded = loginPage.login(credentials.getUsername(),credentials.getPassword());
        Assert.assertEquals(loginSucceeded,credentials.isExpectedToLogin(),"asserting login status");

        if(!loginSucceeded){
            // case log in failed
            // check the error message for failed log in
            softAssert.assertEquals(loginPage.getErrorMessage(),credentials.getError(),"asserting error message");

            //try to navigate to home while you are NOT logged in
            home.navigateToHome();
            softAssert.assertEquals(loginPage.getErrorMessage(),
                    "Epic sadface: You can only access '/inventory.html' when you are logged in.","asserting error message");

            //try to navigate to cart page while you NOT logged in
            cartPage.navigateToCart();
            softAssert.assertEquals(loginPage.getErrorMessage(),
                    "Epic sadface: You can only access '/cart.html' when you are logged in.","asserting error message");

            // Should test all other url here , where user can't access them
            // complete rest of authorization urls access here

            //check all soft assertion errors and obtain test result accordingly
            softAssertionMap.get(uniqueKey).assertAll();

            // remove the soft assert object from the map
            softAssertionMap.remove(uniqueKey);
            return;
        }
        else{
            // Case log in Succeeded

            //We are currently in Home page

            //Check the counter of the cart
            boolean cartCounterIsVisible = home.checkIfShoppingCartCounterIsInvisible();

            //check if cart counter is 0 or invisible
            softAssert.assertEquals(cartCounterIsVisible,false,"Asserting cart count is invisible or count = 0");

            //if not 0 or it's visible then go to cart page and delete all items in the cart
            if(cartCounterIsVisible || home.getShoppingCartCount() >0){
                cartPage.navigateToCart(true);
                softAssert.assertEquals(
                        cartPage.removeAllItemsFromCart(),true,"Asserting deleting all old items from cart");
            }

            //Looping on all items in the test data json file
            for(int i=0;i<items.length;i++){
                //getting the next item name
                String itemName = items[i].getName();

                //Adding this item to the cart and asserting success status
                softAssert.assertEquals(
                        home.addItemToCart(itemName),true,
                        String.format("Asserting adding item: %s to Cart to be true",items[i].getName()));

            }

            cartPage.navigateToCart(true);
            //After adding the items to the cart , asserting that cart counter should equal to the list of expected items count
            softAssert.assertEquals(cartPage.getCartItemsCount(),items.length,"Asserting actual cart items count equals the expected items count");

            //Checking if all items are in cart page
            for (int i=0;i<items.length;i++){
                softAssert.assertEquals(cartPage.isItemInCart(items[i].getName()),
                        true,"Asserting that item: "+items[0].getName()+" is in cart");
            }
            //checking if the check out initiated
            softAssert.assertEquals(cartPage.checkout(),true,"checking if checkout process initiated");

            //Completing checkout and asserting internally that cart is empty after successful checkout
            CheckoutPage checkoutPage = new CheckoutPage(uniqueKey);
            softAssert.assertEquals(checkoutPage.completeCheckout(
                    "Ahmed","Reda","0000"
            ),true,"Asserting that checkout succeeded");

        }




        //Collecting all errors and obtaining test result
        softAssertionMap.get(uniqueKey).assertAll();
        softAssertionMap.remove(uniqueKey);



    }


//------------------------------------------------------------------------------//

    //The goal of this test is to smoke the system behaviour and functionality across all users
    /*Test Steps :
        1- Log in
        2- if the user shouldn't log in :
           2.1 : Try to navigate to all pages and make sure he has no access
        3- if the user logged in successfully , continue

        4- Assert that all items are visible and all data are correct
            against test data : name,price,description,image
            "ONLY the image comparison is implemented due to time"

        5- check the cart counter,
            if the cart counter is not empty, remove all items from it

        6- add all items to the cart and assert in each item addition
        7- Assert the cart counter is equal to item list count
        7- remove all items that has been added to the cart
        8- log out
        9- ensure that user can't access all pages while he is not logged in
     */

    @Test(dataProvider = "getShoppingItemsVsAllCredentials")
    void AddThenRemoveAllItems(CredentialsDTO credentials, ShoppingItemDTO[] items){

        //getting the unique key|current thread to use it as
        //a key to get the browser from the hash map
        //as well as to get the soft assertion object
        String uniqueKey = String.valueOf(Thread.currentThread().getId());
        SoftAssertion softAssert = softAssertionMap.get(uniqueKey);
        LoginPage loginPage =new LoginPage(uniqueKey);
        HomePage home= new HomePage(uniqueKey);
        CartPage cartPage = new CartPage(uniqueKey);

        //log in
        loginPage.navigateToLoginPage(true);
        boolean loginSucceeded = loginPage.login(credentials.getUsername(),credentials.getPassword());
        Assert.assertEquals(loginSucceeded,credentials.isExpectedToLogin(),"asserting login status");

        if(!loginSucceeded){
            // case log in failed
            // check the error message for failed log in
            softAssert.assertEquals(loginPage.getErrorMessage(),credentials.getError(),"asserting error message");

            //try to navigate to home while you are NOT logged in
            home.navigateToHome();
            softAssert.assertEquals(loginPage.getErrorMessage(),
                    "Epic sadface: You can only access '/inventory.html' when you are logged in.","asserting error message");

            //try to navigate to cart page while you NOT logged in
            cartPage.navigateToCart();
            softAssert.assertEquals(loginPage.getErrorMessage(),
                    "Epic sadface: You can only access '/cart.html' when you are logged in.","asserting error message");

            // Should test all other url here , where user can't access them
            // complete rest of authorization urls access here

            //check all soft assertion errors and obtain test result accordingly
            softAssertionMap.get(uniqueKey).assertAll();

            // remove the soft assert object from the map
            softAssertionMap.remove(uniqueKey);
            return;
        }
        else{
            // Case log in Succeeded

            //We are currently in Home page

            //Check the counter of the cart
            boolean cartCounterIsVisible = home.checkIfShoppingCartCounterIsInvisible();

            //check if cart counter is 0 or invisible
            softAssert.assertEquals(cartCounterIsVisible,false,"Asserting cart count is invisible or count = 0");

            //if not 0 or it's visible then go to cart page and delete all items in the cart
            if(cartCounterIsVisible || home.getShoppingCartCount() >0){
                cartPage.navigateToCart(true);
                softAssert.assertEquals(
                        cartPage.removeAllItemsFromCart(),true,"Asserting deleting all old items from cart");
            }

            //Setting the folder that will be used to download actual items images from the website
            String actualImageDownloadFolder=ConfigPropertiesFileHandler.testDataDirPath+"itemsActualImages/"+credentials.getUsername();

            //Looping on all items in the test data json file
            for(int i=0;i<items.length;i++){
                //getting the next item name
                String itemName = items[i].getName();

                //Adding this item to the cart and asserting success status
                softAssert.assertEquals(
                        home.addItemToCart(itemName),true,
                        String.format("Asserting adding item: %s to Cart to be true",items[i].getName()));

                //Downloading the item image and asserting success status
                softAssert.assertEquals(
                        home.downloadItemImage(itemName,actualImageDownloadFolder,itemName),
                        true,"Assert Download Image for Item: "+itemName);

                //Comparing the actual image of the item with the corrsponding expected image that is listed in the test data
                ImageProcessingActions imageProcessingActions = new ImageProcessingActions();
                List<String> imageComparisonResult = imageProcessingActions.compareImagesAndReturnDiffPercentage(
                        items[i].getImageUrl(),
                        actualImageDownloadFolder +"/"+ itemName+".png");


                if(imageComparisonResult!=null){
                    //the image comparison returns a list of results that contains the errors and the percentage
                    //looping on the image comparison results
                    for (String res:imageComparisonResult) {
                        try {
                            //parsing the percentage and asserting if the difference between images is below 20%
                            float f = Float.parseFloat(res);
                            softAssert.assertEquals(
                                    f<20,true,"Asserting that image comparison difference :"+f+"  is less than 20% for image of item: "+itemName
                            );
                            continue;
                        } catch (NumberFormatException nfe) {}

                        //Asserting that there are no errors in reading files or dimensions mismatch
                        softAssert.assertNotEquals(res,"FileIOException","Asserting that image comparison didn't throw IO exception for item: "+ itemName);
                        softAssert.assertNotEquals(res,"Images dimensions mismatch","Asserting that Images dimensions is not mismatch for item: " + itemName );

                    }

                }
                else{
                    //No data was fetched from the image comparison
                    softAssert.assertEquals("No image comparison data","comparison percentage","");
                }
            }

            //After adding the items to the cart , asserting that cart counter should equal to the list of expected items count
            softAssert.assertEquals(home.getShoppingCartCount(),items.length,"Asserting actual cart items count equals the expected items count");

            //looping again on items to delete them from the cart
            for(int i=0;i<items.length;i++) {
                String itemName = items[i].getName();
                softAssert.assertEquals(
                        home.removeItem(itemName), true,
                        String.format("Asserting removing item: %s from Cart to be true", items[i].getName()));
            }
        }
        //Attemping to log out and assert its success
        BurgerMenu burgerMenu = new BurgerMenu(uniqueKey);
        boolean logoutSucceeded = burgerMenu.logout();
        softAssert.assertEquals(logoutSucceeded,true,"Expecting logout to be passed");

        if(logoutSucceeded){
            //if log out succeeded, assert that user can't navigate to either home or cart again
            // unless he log in again
            home.navigateToHome();
            softAssert.assertEquals(loginPage.getErrorMessage(),
                    "Epic sadface: You can only access '/inventory.html' when you are logged in.","asserting error message");

            cartPage.navigateToCart();
            softAssert.assertEquals(loginPage.getErrorMessage(),
                    "Epic sadface: You can only access '/cart.html' when you are logged in.","asserting error message");

        }

        //Collecting all errors and obtaining test result
        softAssertionMap.get(uniqueKey).assertAll();
        softAssertionMap.remove(uniqueKey);



    }

//------------------------------------------------------------------------------//

    @DataProvider(parallel = true)
    public Object[][] getShoppingItemsVsAllCredentials(){
        //A function to return a 2D array of credentials vs items list
        // where each credential data should will be sent a long with all items list

        JSONArray credentials = credentialsFile.getJsonArrayOfNode("credentials");
        JSONArray items = itemsFile.getJsonArrayOfNode("items");

        Object[][] data = new Object[credentials.length()][2];

        ShoppingItemDTO [] itemsList = new ShoppingItemDTO[items.length()];

        //A loop to fill out the items
        for(int j=0;j<items.length();j++){
            JSONObject currentItemJsonObject = items.getJSONObject(j);
            ShoppingItemDTO currentItem = new ShoppingItemDTO(
                    currentItemJsonObject.getString(ShoppingItemDTO.propertiesNames.name.value),
                    currentItemJsonObject.getString(ShoppingItemDTO.propertiesNames.description.value),
                    currentItemJsonObject.getString(ShoppingItemDTO.propertiesNames.price.value),
                    currentItemJsonObject.getString(ShoppingItemDTO.propertiesNames.imageUrl.value));
            itemsList[j]= currentItem;
        }

        //A loop to fill the credentials and attach the full item list to each one of them
        for(int i=0;i<credentials.length();i++){
            CredentialsDTO currentCredentials = new CredentialsDTO();
            JSONObject currentCredentialsJsonObject = credentials.getJSONObject(i);
            currentCredentials.setUsername(currentCredentialsJsonObject.getString(CredentialsDTO.propertiesNames.username.value));
            currentCredentials.setPassword(currentCredentialsJsonObject.getString(CredentialsDTO.propertiesNames.password.value));
            currentCredentials.setExpectedToLogin(currentCredentialsJsonObject.getBoolean(CredentialsDTO.propertiesNames.expectedToLogin.value));
            currentCredentials.setError(currentCredentialsJsonObject.getString(CredentialsDTO.propertiesNames.expectedErrorMessage.value));
            data[i][0]=currentCredentials;
            data[i][1]=itemsList;
        }

        return data;
    }

}
