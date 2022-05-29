package DTOs;

import Actions.UI.WebUIActions;

public class ElementDTO {

    public String selector;
    public WebUIActions.Locators locator;

    public ElementDTO(String selector, WebUIActions.Locators locators) {
        this.selector = selector;
        this.locator = locators;
    }
}
