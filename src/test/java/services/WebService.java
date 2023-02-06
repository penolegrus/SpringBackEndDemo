package services;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;

public abstract class WebService {
    protected String jwt;
    private final String basePath;
    protected RequestSpecification requestSpec;
    public void resetSpec(){
        this.requestSpec = null;
        setSpec();
    }
    private void setSpec(){
        this.requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .filters(new ResponseLoggingFilter())
                .basePath(basePath);
    }
    public WebService(String basePath){
        this.basePath = basePath;
        setSpec();
    }



}
