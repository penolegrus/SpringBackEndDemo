package controllers;

import app.UserDataBase;
import app.UserGameService;
import helpers.Utils;
import jwt.config.JwtTokenUtil;
import models.InfoMessage;
import models.Message;
import models.User;
import models.game.DLC;
import models.game.Game;
import models.game.UpdField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static app.UserDataBase.isUserInBaseUsers;

@RestController
public class GameController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private User getUserFromJwt(String jwt) {
        jwt = jwt.replace("Bearer ", "");
        String getUserLoginFromToken = jwtTokenUtil.getUsernameFromToken(jwt);
        return UserDataBase.getUser(getUserLoginFromToken);
    }

    @GetMapping(path = "/api/user/games", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getGames(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);
        return ResponseEntity.status(200).body(user.getGames());
    }

    @GetMapping(path = "/api/user/games/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getGames(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        Game game = new UserGameService(user).findGameById(id);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }
        return ResponseEntity.status(200).body(game);
    }


    @DeleteMapping(path = "/api/user/games/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteGame(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        UserGameService userGameService = new UserGameService(user);
        Game game = userGameService.findGameById(id);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }

        if (isUserInBaseUsers(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete game from base users"));
        }

        userGameService.deleteGame(id);
        return ResponseEntity.status(200).body(new InfoMessage("success", "Game successfully deleted"));
    }

    @DeleteMapping(path = "/api/user/games/{id}/dlc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteDlc(@PathVariable Integer id, @RequestBody(required = false) List<DLC> dls,
                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        UserGameService userGameService = new UserGameService(user);
        Game game = userGameService.findGameById(id);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }

        if (isUserInBaseUsers(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete game from base users"));
        }

        if (dls == null || dls.isEmpty()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "List with DLC to delete cant be empty or null"));
        }

        userGameService.deleteDlc(id, dls);
        return ResponseEntity.status(200).body(new InfoMessage("success", "Game DLC successfully deleted"));
    }

    @PostMapping(path = "/api/user/games", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addGame(@RequestBody Game newGame, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        UserGameService userGameService = new UserGameService(user);

        if(!userGameService.isLimitForGames()){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Limit of games, user can have only 20 games"));
        }

        Map<String, Object> response = new HashMap<>();

        if (!userGameService.isPricesAndIsFreeCorrect(newGame)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Free DLC or Game cant have price more than 0.0$"));
        }

        newGame.setGameId(Utils.getRandomInt());
        response.put("info", new Message("success", "Game created"));
        response.put("register_data", newGame);


        userGameService.addGame(newGame);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping(path = "/api/user/games/{gameId}/updateField", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateGameField(@PathVariable Integer gameId, @RequestBody UpdField updField,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        UserGameService userGameService = new UserGameService(user);

        Game oldGame = userGameService.findGameById(gameId);

        if (oldGame == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }

        if (isUserInBaseUsers(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant update game field from base users"));
        }

        if (updField.getFieldName().equalsIgnoreCase("gameId")) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cannot edit ID field"));
        }

        if (!oldGame.isFieldExist(updField.getFieldName())) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cannot find field"));
        }

        if (!oldGame.isTypeOfNewFieldCorrect(updField)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cannot set new value because field has incorrect type"));
        }

        if (oldGame.isNewFieldHasSameValue(updField.getFieldName(), updField.getValue())) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "New field value is same as before"));
        }

        Game updated = oldGame.editFieldValue(updField.getFieldName(), updField.getValue());

        userGameService.updateGame(updated);
        return ResponseEntity.status(200).body(new InfoMessage("success", "New value edited successfully on field " + updField.getFieldName()));
    }

    @PutMapping(path = "/api/user/games/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateGameDlcInfo(@PathVariable Integer gameId, @RequestBody(required = false) List<DLC> dlcs,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        UserGameService userGameService = new UserGameService(user);

        Game game = userGameService.findGameById(gameId);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }

        if (isUserInBaseUsers(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant update dlc game from base users"));
        }


        if (dlcs == null || dlcs.isEmpty()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Empty body with list of dlc to modify"));
        }

        List<DLC> dlsUpdated = game.getDlcs();
        dlsUpdated.addAll(dlcs);

        game.setDlcs(new ArrayList<>(new HashSet<>(dlsUpdated)));

        userGameService.updateGame(game);
        return ResponseEntity.status(200).body(new InfoMessage("success", "DlC successfully changed"));
    }
}
