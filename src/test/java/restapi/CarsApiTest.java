package restapi;

import models.trains.CarBrands;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static helpers.Constants.CAR_BRAND_API_URL;
import static io.restassured.RestAssured.given;

public class CarsApiTest extends BaseApiTest {

    private static List<CarBrands> carBrands;

    @BeforeAll
    public static void getCats(){
        carBrands = restService.get(CAR_BRAND_API_URL).asList(CarBrands.class);
    }

    @Test
    public void carBrandBmwExists() {
        String carBrand = "bmw";

        boolean isX3Exists = carBrands.stream()
                .anyMatch(x -> x.getBrand().equalsIgnoreCase(carBrand));

        Assert.assertTrue(isX3Exists, "Модели " + carBrand + " нет в списке");
    }

    @Test
    public void carsBmwXSeries() {
        String carBrand = "bmw";

        List<String> xSeries = carBrands.stream()
                .filter(x -> x.getBrand().equalsIgnoreCase(carBrand))
                .findAny().orElseThrow(() -> new AssertionError("Машин " + carBrand + " нет в списке"))
                .getModels().stream()
                .filter(x -> x.startsWith("X")).collect(Collectors.toList());

        Assert.assertEquals(xSeries.size(), 5, "Моделей серии X не 5 штук");
    }

    @Test
    public void mercedesSizeTest() {
        String carBrand = "Mercedes-Benz";

        long carsCount = carBrands.stream()
                .filter(x -> x.getBrand().equalsIgnoreCase(carBrand))
                .findAny().orElseThrow(() -> new AssertionError("Машин " + carBrand + " нет в списке"))
                .getModels().size();

        Assert.assertEquals(carsCount, 58, "Количество машин не совпадает с ожидаемым результатом");
    }
}
