package services;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;

public abstract class WebService {
    protected static String jwt;
    public String basePath;
    public RequestSpecification requestSpec;
    public final String defaultContentType = "application/json; charset=utf-8";
    public void resetSpec(){
        this.requestSpec = null;
        setSpec();
    }
    private void setSpec(){
        this.requestSpec = RestAssured.given()
                .contentType(defaultContentType)
                .filters(new ResponseLoggingFilter())
                .basePath(basePath);
    }
    public WebService(String basePath){
        this.basePath = basePath;
        setSpec();
    }



}
