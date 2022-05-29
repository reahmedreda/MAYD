package DTOs;
import org.json.JSONArray;
import org.json.JSONObject;

public class GenericApiResponseDTO {

    int statusCode;
    String errors;
    JSONObject body;
    JSONArray bodyAsArray;

    public GenericApiResponseDTO(){};

    public GenericApiResponseDTO(int statusCode){
        this.statusCode=statusCode;
    }

    public GenericApiResponseDTO(int statusCode,JSONObject body){
        this.body=body;
        this.statusCode=statusCode;
    }

    public GenericApiResponseDTO(int statusCode,JSONArray body){
        this.bodyAsArray=body;
        this.statusCode=statusCode;
    }

    public GenericApiResponseDTO(int statusCode,JSONObject body,String errors){
        this.body=body;
        this.statusCode=statusCode;
        this.errors=errors;
    }


    public int getStatusCode(){
        return statusCode;
    }

    public void setStatusCode(int statusCode){
        this.statusCode =statusCode;
    }

    public String getErrors(){
        return errors;
    }

    public void setErrors(String errors){
        this.errors=errors;
    }


    public JSONObject getBody(){
        return body;
    }

    public JSONArray getBodyAsArray(){
        return bodyAsArray;
    }

    public void setBody(JSONObject body){
        this.body=body;
    }

}
