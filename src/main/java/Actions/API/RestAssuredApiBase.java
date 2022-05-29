package Actions.API;

import DTOs.GenericApiRequestDTO;
import DTOs.GenericApiResponseDTO;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class RestAssuredApiBase {


    public GenericApiResponseDTO sendRequest(GenericApiRequestDTO request){
        GenericApiResponseDTO responseDTO =new GenericApiResponseDTO();
        RequestSpecification requestSpecification = given()
                .headers(request.getHeaders())
                .when()
                .body(request.getBody().toString());

        Response response = null;

        try {
            switch (request.getRequestType()) {
                case GET:
                    response = requestSpecification.get(request.getUrl());
                    break;
                case PUT:
                    response = requestSpecification.put(request.getUrl());
                    break;
                case POST:
                    response = requestSpecification.post(request.getUrl());
                    break;
                case DELETE:
                    response = requestSpecification.delete(request.getUrl());
                    break;
            }
        }
        catch(Exception e){
            responseDTO.setErrors(e.getMessage());
        }

        if(response!=null) {
            response = response.then().extract().response();
            int statusCode =response.getStatusCode();
            if(statusCode==200) {
                ResponseBody res = response.getBody();
                String s = res.asPrettyString();
                try{
                    JSONObject responseBody = new JSONObject(s);
                    return new  GenericApiResponseDTO(statusCode, responseBody);
                }
                catch(JSONException e){
                    JSONArray responseBody = new JSONArray(s);
                    return new  GenericApiResponseDTO(statusCode, responseBody);
                }
                catch (Exception e){
                    responseDTO.setErrors(e.getMessage());
                }
//                if (s.charAt(0) == '{') {
//                    JSONObject responseBody = new JSONObject(res.asPrettyString());
//                    return new  GenericApiResponseDTO(statusCode, responseBody);
//
//                } else if (s.charAt(0) == '[') { // to deal when the response comes as an array of json objects
//                    JSONArray responseBody = new JSONArray(res.asPrettyString());
//                    return new  GenericApiResponseDTO(statusCode, responseBody);
//                }
            }
            return new GenericApiResponseDTO(statusCode);
        }
        else{
            responseDTO.setErrors("API call failed");
        }

        return responseDTO;
    }

    public Map<String,?> generateUsualHeaders(String token,String contentType,String accept){
        return   Map.of(
                "Authorization",token,
                "Content-Type",contentType,
                "Accept",accept);
    }

    public enum ContentType1 {
        URL_ENCODED("application/x-www-form-urlencoded"),
        RAW("application/json");

        private final String value;

        ContentType1(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
