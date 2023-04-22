package new_controllers;

import db_models.game.DLC;
import db_models.game.Game;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import models.InfoMessage;
import models.game.RegisterGameResponse;
import models.game.UpdField;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.GameService;


import java.util.List;
import java.util.Set;


@RestController
@RequiredArgsConstructor
public class GameControllerNew {
    private final GameService gameService;

    @Operation(description = "Получает все игры пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "Список с играми у пользователя",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Game[].class)))

    @GetMapping(path = "/api/user/games", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Game>> getGames(@Parameter(description = "Jwt токен")
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return gameService.getGames(authHeader);
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
    public ResponseEntity<?> getGame(@Parameter(description = "id игры") @PathVariable Long id,
                                     @Parameter(description = "Jwt токен")
                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return gameService.getGame(id, authHeader);
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
        return gameService.addGame(newGame, authHeader);
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
    public ResponseEntity<InfoMessage> deleteGame(@Parameter(description = "id игры") @PathVariable Long id,
                                                  @Parameter(description = "Jwt токен")
                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return gameService.deleteGame(id, authHeader);
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
    public ResponseEntity<InfoMessage> deleteDlc(@Parameter(description = "id игры") @PathVariable Long id,
                                                 @Parameter(description = "Список с DLC для удаления (может быть 1 DLC в списке")
                                                 @RequestBody(required = false) List<DLC> dls,
                                                 @Parameter(description = "Jwt токен")
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return gameService.deleteDlc(id,dls,authHeader);
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
    public ResponseEntity<InfoMessage> updateGameField(@Parameter(description = "id игры") @PathVariable Long gameId,
                                                       @Parameter(description = "обновляемое поле") @RequestBody UpdField updField,
                                                       @Parameter(description = "Jwt токен")
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
       return gameService.updateGameField(gameId,updField,authHeader);
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
                                                         @PathVariable Long gameId,
                                                         @Parameter(description = "Список с DLC для обнолений")
                                                         @RequestBody(required = false) List<DLC> dlcs,
                                                         @Parameter(description = "Jwt токен")
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
       return gameService.updateGameDlcInfo(gameId, dlcs, authHeader);
    }
}
