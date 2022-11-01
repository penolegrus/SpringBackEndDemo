package services;

import assections.AssertableResponse;

public class JsonService extends WebService{
    public JsonService(String basePath) {
        super(basePath);
    }

    public AssertableResponse getVersion(){
        return new AssertableResponse(requestSpec.get("version").then());
    }

    public AssertableResponse getNums(){
        return new AssertableResponse(requestSpec.get("nums").then());
    }

    public AssertableResponse getCarBrands(){
        return new AssertableResponse(requestSpec.get("carBrands").then());
    }
}
