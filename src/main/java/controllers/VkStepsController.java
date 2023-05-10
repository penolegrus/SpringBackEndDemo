package controllers;

import io.restassured.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import models.messages.InfoMessage;
import models.VkSteps;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

@RestController
public class VkStepsController {
    @Operation(description = "Устанавливает количество шагов вконтакте через API ключ. Ключ можно получить по этому адресу, подставив свой логин и пароль\n" +
            "https://api.vk.com/oauth/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&username={user}&password={pass}\n" +
            "Максимум 86000 шагов, установить шаги можно только 1 раз в сутки")
    @PostMapping(path = "/api/vk/steps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> setVkSteps(@Parameter(name = "Количество шагов и api ключ")
                                                      @RequestBody VkSteps vkSteps) {
        if(vkSteps.getSteps() >= 86000) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "You cant add more than 86000 steps"));
        }

        if(vkSteps.getApiKey() == null || vkSteps.getApiKey().isEmpty()){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "You need to set api key"));
        }

        String userAgent = "VKAndroidApp/7.7-10445 (Android 11; SDK 30; arm64-v8a; Xiaomi M2003J15SC; ru; 2340x1080";
        String dateNow = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);

        double partFormula = Double.parseDouble(String.valueOf(vkSteps.getSteps()));
        String km = String.valueOf(partFormula / 1250*1000);
        String endPoint = String.format("https://api.vk.com/method/vkRun.setSteps?steps=%s&distance=%s&date=%s&access_token=%s&v=5.131",
                vkSteps.getSteps(), km,dateNow,vkSteps.getApiKey());
        Response response = given().header("User-Agent", userAgent).get(endPoint).then().extract().response();
        if(response.statusCode() == 200){
            return ResponseEntity.status(200).body(response.asPrettyString());
        } else {
            return ResponseEntity.status(400).body(response.asPrettyString());
        }
    }
}
