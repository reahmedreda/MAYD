package DTOs;

import java.text.NumberFormat;

public class ShoppingItemDTO {
    String name,description,imageUrl;
    float price;


    public ShoppingItemDTO(String name, String description, float price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ShoppingItemDTO(String name, String description, String price,String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = stripPriceFromSpecialChar(price);
        this.imageUrl = imageUrl;
    }


    private float stripPriceFromSpecialChar(String price){
        return Float.valueOf(price.replaceAll("[^\\d.]", ""));

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public enum propertiesNames{
        name("name"),
        price("price"),
        description("description"),
        imageUrl("imageUrl");

        public String value;
        propertiesNames(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
