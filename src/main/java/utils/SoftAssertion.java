package utils;


import org.assertj.core.api.SoftAssertions;

public class SoftAssertion {

    SoftAssertions softAssertions= new SoftAssertions();

    public void init(){
        softAssertions = new SoftAssertions();
    }

    public void assertAll(){
        softAssertions.assertAll();
    }

    public void assertEquals(Object actual, Object expected,String message){
        softAssertions.assertThat(actual).withFailMessage(String.format("%s \n Expected value: %s ,\n but found value: %s",message,expected,actual)).isEqualTo(expected);
    }

    public void assertNotEquals(Object actual, Object expected,String message){
        softAssertions.assertThat(actual).withFailMessage(String.format("%s \n Expected value: %s ,\n but found value: %s",message,expected,actual)).isNotEqualTo(expected);
    }

    public void assertNotNull(Object actual,String message){
        softAssertions.assertThat(actual).withFailMessage(String.format("%s \n Expected value: %s to be not null",message,actual)).isNotNull();
    }

    public void assertNull(Object actual,String message){
        softAssertions.assertThat(actual).withFailMessage(String.format("%s \n Expected value: %s to be not null",message,actual)).isNull();
    }
}
