package tests;

import assections.Conditions;
import com.fasterxml.jackson.core.type.TypeReference;
import core.TestBase;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.trains.ApiVersion;
import models.trains.CarBrands;
import models.trains.NumbersPow;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import services.JsonService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class JsonTrainTests extends TestBase {
    private final JsonService jsonService = new JsonService();

    @Test
    public void redirectTest(){
        jsonService.getRedirect301()
                .shouldHave(Conditions.statusCode(301))
                .shouldHave(Conditions.header("Location", "https://www.youtube.com/@net_vlador"));
    }

    @Test
    public void apiVersionTest(){
        //ApiVersion version = jsonService.getVersion().as(ApiVersion.class);
        //Assertions.assertTrue(version.getApiVersion().matches("^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$"));

        ApiVersion versionGeneric = jsonService.getVersionWithGeneric().asObject();
        Assertions.assertTrue(versionGeneric.getApiVersion().matches("^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$"));
        System.out.println(versionGeneric);
    }

    @Test
    public void numbersTest(){
        NumbersPow value = jsonService.getNums().as("numbersPow.nums", NumbersPow.class);
        System.out.println(value);
    }

    @Test
    public void carsTest(){
        List<CarBrands> cars = jsonService.getCarBrandsGeneric().asObject();

        String bmwBrand = "bmw";
        boolean isX3Exists = cars.stream()
                .anyMatch(x -> x.getBrand().equalsIgnoreCase(bmwBrand));

        Assert.assertTrue(isX3Exists, "Модели " + bmwBrand + " нет в списке");

        List<String> xSeries = cars.stream()
                .filter(x -> x.getBrand().equalsIgnoreCase(bmwBrand))
                .findAny().orElseThrow(() -> new AssertionError("Машин " + bmwBrand + " нет в списке"))
                .getModels().stream()
                .filter(x -> x.startsWith("X")).collect(Collectors.toList());

        Assert.assertEquals(xSeries.size(), 5, "Моделей серии X не 5 штук");

        String carBrand = "Mercedes-Benz";

        long carsCount = cars.stream()
                .filter(x -> x.getBrand().equalsIgnoreCase(carBrand))
                .findAny().orElseThrow(() -> new AssertionError("Машин " + carBrand + " нет в списке"))
                .getModels().size();

        Assert.assertEquals(carsCount, 58, "Количество машин не совпадает с ожидаемым результатом");
    }

}
