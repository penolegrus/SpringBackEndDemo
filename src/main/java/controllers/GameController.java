package controllers;

import app.GamesDataBase;
import helpers.Utils;
import models.InfoMessage;
import models.Message;
import models.game.DLC;
import models.game.Game;
import models.game.UpdField;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static helpers.Constants.*;

@RestController
public class GameController {
    private final int limitToDelete = 200;
    private final GamesDataBase gameDataBase = new GamesDataBase();

    @GetMapping(path = GAME_API_URL + "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getGame(@PathVariable Integer gameId, HttpServletResponse httpServletResponse) {
        Game game = gameDataBase.findGameById(gameId);
        if (game == null) {
            httpServletResponse.setStatus(400);
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game not found"));
        }
        return ResponseEntity.status(200).body(game);
    }

    @GetMapping(path = GAME_API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Game> getGames() {
        List<Game> games = gameDataBase.getAllGames();

        if (games.size() > limitToDelete) {
            while (games.size() >= 100) {
                games.remove(games.size() - 1);
            }
        }

        gameDataBase.updateDataBase(games);
        return games;
    }

    @DeleteMapping(path = GAME_API_URL + "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteGame(@PathVariable Integer gameId) {
        if (!gameDataBase.isGameExist(gameId)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game not found to delete"));
        }
        if (gameDataBase.isGameInBaseGames(gameId)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete base games"));
        }

        gameDataBase.deleteGame(gameId);
        return ResponseEntity.status(200).body(new InfoMessage("success", "Game successfully deleted"));
    }

    @DeleteMapping(path = GAME_API_URL + "/{gameId}/dlc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteDlc(@PathVariable Integer gameId, @RequestBody(required=false) List<DLC> dls) {
        if (!gameDataBase.isGameExist(gameId)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game not found to delete dlc"));
        }

        if (gameDataBase.isGameInBaseGames(gameId)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete dlc from base games"));
        }

        if(dls == null || dls.isEmpty()){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "List with DLC to delete cant be empty or null"));
        }

        gameDataBase.deleteDlc(gameId, dls);
        return ResponseEntity.status(200).body(new InfoMessage("success", "Game DLC successfully deleted"));
    }

    @PostMapping(path = {GAME_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addGame(@RequestBody Game game) {
        JSONObject response = new JSONObject();

        if(!gameDataBase.isPricesAndIsFreeCorrect(game)){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Free DLC or Game cant have price more than 0.0$"));
        }

        game.setGameId(Utils.getRandomInt());
        response.put("info", new JSONObject((new Message("success", "Game created"))));
        response.put("register_data", new JSONObject(game));


        gameDataBase.addGame(game);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping(path = GAME_API_URL + "/{gameId}/updateField", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateGameField(@PathVariable Integer gameId, @RequestBody UpdField updField) {
        Game oldGame = gameDataBase.findGameById(gameId);
        if(updField.getFieldName().equalsIgnoreCase("gameId")) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cannot edit ID field"));
        }

        if(!oldGame.isFieldExist(updField.getFieldName())){

            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cannot find field"));
        }

        if(!oldGame.isTypeOfNewFieldCorrect(updField)){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cannot set new value because field has incorrect type"));
        }

        if(oldGame.isNewFieldHasSameValue(updField.getFieldName(),updField.getValue())){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "New field value is same as before"));
        }

        Game updated = oldGame.editFieldValue(updField.getFieldName(),updField.getValue());

        gameDataBase.updateGame(updated);
        return ResponseEntity.status(200).body(new InfoMessage("success", "New value edited successfully on field " + updField.getFieldName()));
    }

    @PutMapping(path = GAME_API_URL + "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateGameDlcInfo(@PathVariable Integer gameId, @RequestBody(required = false) List<DLC> dlcs) {
        Game oldGame = gameDataBase.findGameById(gameId);

        if (oldGame == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game not found to modify"));
        }

        if (gameDataBase.isGameInBaseGames(gameId)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant modify base games"));
        }

        if(dlcs == null || dlcs.isEmpty()){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Empty body with list of dlc to modify"));
        }

        List<DLC> dlsUpdated = oldGame.getDlcs();
        dlsUpdated.addAll(dlcs);

        oldGame.setDlcs(new ArrayList<>(new HashSet<>(dlsUpdated)));

        gameDataBase.updateGame(oldGame);
        return ResponseEntity.status(200).body(new InfoMessage("success", "DlC successfully changed"));
    }
}
