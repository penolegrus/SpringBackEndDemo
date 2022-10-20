package controllers;

import app.JsonDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.JsonResourceHelper;
import models.CarBrands;
import models.MixedFields;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static helpers.Constants.*;

@RestController
public class JsonsTrainController {

    private final JsonDB jsonDB = new JsonDB();
    private final ObjectMapper mapper = new ObjectMapper();


    @GetMapping(path = {CAR_BRAND_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CarBrands> easy1() throws JsonProcessingException {
        return mapper.readerForListOf(CarBrands.class).readValue(jsonDB.carBrands);
    }

    @GetMapping(path = {NUMS_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MixedFields easy2() throws JsonProcessingException {
        return mapper.readValue(jsonDB.mixedFieldsJson,MixedFields.class);
    }

}
