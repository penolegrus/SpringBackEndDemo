package services;

import assections.AssertableResponse;
import assections.GenericResponse;
import io.restassured.common.mapper.TypeRef;
import models.trains.ApiVersion;
import models.trains.CarBrands;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonService extends WebService{
    public JsonService() {
        super("/easy/");
    }

    public AssertableResponse getVersion(){
        return new AssertableResponse(requestSpec.get("version").then());
    }

    public GenericResponse<ApiVersion> getVersionWithGeneric(){
        return new GenericResponse<>(requestSpec.get("version").then(), new TypeRef<ApiVersion>(){});
    }

    public AssertableResponse getNums(){
        return new AssertableResponse(requestSpec.get("nums").then());
    }

    public AssertableResponse getCarBrands(){
        return new AssertableResponse(requestSpec.get("carBrands").then());
    }

    public GenericResponse<List<CarBrands>> getCarBrandsGeneric(){
        return new GenericResponse<>(requestSpec.get("carBrands").then(), new TypeRef<List<CarBrands>>() {});
    }

    public AssertableResponse getRedirect301(){
        return new AssertableResponse(requestSpec.redirects().follow(false).get("redirect").then());
    }
}
