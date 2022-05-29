package DTOs;

import java.util.Map;

public class GenericApiRequestDTO {

    String url,cookies="";
    Object body;
    Map<String,?> headers;
    RequestType requestType;


    public GenericApiRequestDTO(String url, String cookies, Object body, Map<String, ?> headers, RequestType requestType) {
        this.url = url;
        this.cookies = cookies;
        this.body = body;
        this.headers = headers;
        this.requestType = requestType;
    }

    public GenericApiRequestDTO(String url, Object body, Map<String, ?> headers, RequestType requestType) {
        this.url = url;
        this.body = body;
        this.headers = headers;
        this.requestType = requestType;
    }


    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url =url;
    }


    public Object getBody(){
        return body;
    }

    public void setBody(Object body){
        this.body=body;
    }

    public Map<String, ?> getHeaders(){
        return headers;
    }

    public void setHeaders(Map<String, ?> headers){
        this.headers=headers;
    }

    public RequestType getRequestType(){
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }


    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public enum RequestType{
        GET,
        POST,
        PUT,
        DELETE
    };

}
