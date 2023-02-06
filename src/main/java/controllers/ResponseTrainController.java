package controllers;

import app.JsonDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import models.trains.ApiVersion;
import models.trains.CarBrands;
import models.trains.MixedFields;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static helpers.Constants.*;

@RestController
public class ResponseTrainController {

    private final JsonDB jsonDB = new JsonDB();
    private final ObjectMapper mapper = new ObjectMapper();

    @Operation(summary = "Получает список с машинами")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Машины различных брендов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CarBrands[].class)
                            )
                    })
    })
    @GetMapping(path = {CAR_BRAND_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<CarBrands>> easy1() throws JsonProcessingException {
        return ResponseEntity.status(200).body(mapper.readerForListOf(CarBrands.class).readValue(jsonDB.carBrands));
    }

    @Operation(summary = "Получает различные варианты ключей для тренировки извлечения ответов RestAssured")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Модный JSON",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MixedFields.class)
                            )
                    })
    })
    @GetMapping(path = {NUMS_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<MixedFields> easy2() throws JsonProcessingException {
        return ResponseEntity.status(200).body(mapper.readValue(jsonDB.mixedFieldsJson, MixedFields.class));
    }

    @Operation(summary = "Получает актуальную версию API приложения")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Актуальная версия API",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiVersion.class)
                            )
                    })
    })
    @GetMapping(path = "/api/easy/version", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ApiVersion> getCurrentApiVersion() {
        return ResponseEntity.status(200).body(new ApiVersion("1.0.2"));
    }

    @Operation(summary = "Перенаправляет на определенный адрес и возвращает статус код 301")
    @GetMapping(path = "/api/easy/redirect", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ModelAndView redirectTo301(){
        RedirectView rv = new RedirectView();
        rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        rv.setUrl("https://www.youtube.com/@net_vlador");
        return new ModelAndView(rv);
    }
}
