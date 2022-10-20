package controllers;

import app.GamesDataBase;
import helpers.InfoResponse;
import helpers.Utils;
import models.Message;
import models.game.DLC;
import models.game.Game;
import models.game.UpdField;
import org.json.JSONObject;
import org.springframework.http.MediaType;
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
    public String getGame(@PathVariable Integer gameId, HttpServletResponse httpServletResponse) {
        Game game = gameDataBase.findGameById(gameId);
        if (game == null) {
            httpServletResponse.setStatus(400);
            return new InfoResponse("fail", "Game not found").toString();
        }
        return game.toString();
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
    public String deleteGame(@PathVariable Integer gameId, HttpServletResponse servlet) {
        if (!gameDataBase.isGameExist(gameId)) {
            servlet.setStatus(400);
            return new InfoResponse("fail", "Game not found to delete").toString();
        }
        if (gameDataBase.isGameInBaseGames(gameId)) {
            servlet.setStatus(400);
            return new InfoResponse("fail", "Cant delete base games").toString();
        }

        gameDataBase.deleteGame(gameId);
        return new InfoResponse("success", "Game successfully deleted").toString();
    }

    @DeleteMapping(path = GAME_API_URL + "/{gameId}/dlc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteDlc(@PathVariable Integer gameId, @RequestBody(required=false) List<DLC> dls, HttpServletResponse servlet) {
        if (!gameDataBase.isGameExist(gameId)) {
            servlet.setStatus(400);
            return new InfoResponse("fail", "Game not found to delete dlc").toString();
        }

        if (gameDataBase.isGameInBaseGames(gameId)) {
            servlet.setStatus(400);
            return new InfoResponse("fail", "Cant delete dlc from base games").toString();
        }

        if(dls == null || dls.isEmpty()){
            servlet.setStatus(400);
            return new InfoResponse("fail", "List with DLC to delete cant be empty or null").toString();
        }

        gameDataBase.deleteDlc(gameId, dls);
        return new InfoResponse("success", "Game DLC successfully deleted").toString();
    }

    @PostMapping(path = {GAME_API_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addGame(@RequestBody Game game, HttpServletResponse responseServlet) {
        JSONObject response = new JSONObject();

        if(!gameDataBase.isPricesAndIsFreeCorrect(game)){
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "Free DLC or Game cant have price more than 0.0$").toString();
        }

        game.setGameId(Utils.getRandomInt());
        response.put("info", new JSONObject((new Message("success", "Game created"))));
        response.put("register_data", new JSONObject(game));
        responseServlet.setStatus(201);

        gameDataBase.addGame(game);

        return response.toString();
    }

    @PutMapping(path = GAME_API_URL + "/{gameId}/updateField", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateGameField(@PathVariable Integer gameId, @RequestBody UpdField updField, HttpServletResponse responseServlet) {
        Game oldGame = gameDataBase.findGameById(gameId);
        if(updField.getFieldName().equalsIgnoreCase("gameId")) {
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "Cannot edit ID field").toString();
        }

        if(!oldGame.isFieldExist(updField.getFieldName())){
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "Cannot find field").toString();
        }

        if(!oldGame.isTypeOfNewFieldCorrect(updField)){
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "Cannot set new value because field has incorrect type").toString();
        }

        if(oldGame.isNewFieldHasSameValue(updField.getFieldName(),updField.getValue())){
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "New field value is same as before").toString();
        }

        Game updated = oldGame.editFieldValue(updField.getFieldName(),updField.getValue());

        gameDataBase.updateGame(updated);
        return new InfoResponse("success", "New value edited successfully on field " + updField.getFieldName()).toString();
    }

    @PutMapping(path = GAME_API_URL + "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateGameDlcInfo(@PathVariable Integer gameId, @RequestBody(required = false) List<DLC> dlcs, HttpServletResponse responseServlet) {
        Game oldGame = gameDataBase.findGameById(gameId);

        if (oldGame == null) {
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "Game not found to modify").toString();
        }

        if (gameDataBase.isGameInBaseGames(gameId)) {
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "Cant modify base games").toString();
        }

        if(dlcs == null || dlcs.isEmpty()){
            responseServlet.setStatus(400);
            return new InfoResponse("fail", "Empty body with list of dlc to modify").toString();
        }

        List<DLC> dlsUpdated = oldGame.getDlcs();
        dlsUpdated.addAll(dlcs);

        oldGame.setDlcs(new ArrayList<>(new HashSet<>(dlsUpdated)));

        gameDataBase.updateGame(oldGame);
        return new InfoResponse("success", "DlC successfully changed").toString();
    }
}
