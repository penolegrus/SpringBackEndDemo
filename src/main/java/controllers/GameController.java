package controllers;

import app.UserDataBase;
import app.UserGameService;
import helpers.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jwt.config.JwtTokenUtil;
import models.InfoMessage;
import models.Message;
import models.game.RegisterGameResponse;
import models.user.User;
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

    @Operation(description = "Получает все игры пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "Список с играми у пользователя",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game[].class)))

    @GetMapping(path = "/api/user/games", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Game>> getGames(@Parameter(description = "Jwt токен")
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);
        return ResponseEntity.status(200).body(user.getGames());
    }

    @Operation(description = "Получает конкретную игру у пользователя по ID игры")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Игра успешно отобразилась",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Игра не существует",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @GetMapping(path = "/api/user/games/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getGame(@Parameter(description = "id игры") @PathVariable Integer id,
                                      @Parameter(description = "Jwt токен")
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        Game game = new UserGameService(user).findGameById(id);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }
        return ResponseEntity.status(200).body(game);
    }


    @Operation(description = "Удаляет игру у пользователя по ID игры")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Игра успешно удалилась",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неудачная попытка удалить несуществующую или базовую игру",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @DeleteMapping(path = "/api/user/games/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<InfoMessage> deleteGame(@Parameter(description = "id игры") @PathVariable Integer id,
                                                  @Parameter(description = "Jwt токен")
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

        userGameService.deleteGame(id);
        return ResponseEntity.status(200).body(new InfoMessage("success", "Game successfully deleted"));
    }

    @Operation(description = "Удаляет DLC у игры у пользователя по ID игры")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "DLC у игры успешно удалено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неудачная попытка удалить DLC у несуществующей или базовой игры, или список с DLC пуст",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @DeleteMapping(path = "/api/user/games/{id}/dlc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<InfoMessage> deleteDlc(@Parameter(description = "id игры") @PathVariable Integer id,
                                                 @Parameter(description = "Список с DLC для удаления (может быть 1 DLC в списке")
                                                 @RequestBody(required = false) List<DLC> dls,
                                                 @Parameter(description = "Jwt токен")
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

    @Operation(description = "Добавляет игру пользователю")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Игра успешно добавлена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegisterGameResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неудачная попытка добавить игру. Лимит игр 20 штук у пользователя или неверно заполнены поля у игры",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @PostMapping(path = "/api/user/games", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addGame(@Parameter(description = "Добавляемая игра") @RequestBody Game newGame,
                                     @Parameter(description = "Jwt токен") @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        UserGameService userGameService = new UserGameService(user);

        if (!userGameService.isLimitForGames()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Limit of games, user can have only 20 games"));
        }

        if (!userGameService.isPricesAndIsFreeCorrect(newGame)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Free DLC or Game cant have price more than 0.0$"));
        }

        newGame.setGameId(Utils.getRandomInt());

        userGameService.addGame(newGame);

        return ResponseEntity.status(201).body(new RegisterGameResponse(newGame, new Message("success", "Game created")));
    }

    @Operation(description = "Обновляет поле у игры")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Поле у игры успешно обновлено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неудачная попытка обновить поле у игры. Неверное значение поля и тип данных или попытка редактировать базовую игру",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @PutMapping(path = "/api/user/games/{gameId}/updateField", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<InfoMessage> updateGameField(@Parameter(description = "id игры") @PathVariable Integer gameId,
                                                       @Parameter(description = "обновляемое поле") @RequestBody UpdField updField,
                                                       @Parameter(description = "Jwt токен")
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

    @Operation(description = "Обновляет полностью список DLC у игры")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список DLC у игры успешно изменен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неудачная попытка обновить DLC у игры. Игра несуществует или неверно описана модель DLC для обновления",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @PutMapping(path = "/api/user/games/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<InfoMessage> updateGameDlcInfo(@Parameter(description = "id игры")
                                                         @PathVariable Integer gameId,
                                                         @Parameter(description = "Список с DLC для обнолений")
                                                         @RequestBody(required = false) List<DLC> dlcs,
                                                         @Parameter(description = "Jwt токен")
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
