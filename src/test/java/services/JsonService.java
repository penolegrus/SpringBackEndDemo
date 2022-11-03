package services;

import assections.AssertableResponse;

public class JsonService extends WebService{
    public JsonService() {
        super("/easy/");
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

    public AssertableResponse getRedirect301(){
        return new AssertableResponse(requestSpec.redirects().follow(false).get("redirect").then());
    }
}
