package tests;

import core.TestBase;
import dto.UserDTO;
import helpers.Utils;
import db_models.game.DLC;
import models.game.Game;
import models.game.UpdField;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import services.GameService;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static assections.Conditions.hasMessage;
import static assections.Conditions.statusCode;
import static testdata.TestData.ADMIN_USER;

public class GamesApiTest extends TestBase {

    private final GameService gameService = new GameService();

    private Game addRandomGame(boolean witDlc) {
        return gameService.addRandomGame(witDlc).as("register_data", Game.class);
    }

    @Test
    public void getGamesDb(){
        userService.login(new UserDTO("user5","keks", new ArrayList<>()));
        List<Game> games = gameService.getGames().asList(Game.class);
        gameService.getGame(games.stream().filter(x->x.getGameId().equals(26))
                .findFirst().orElseThrow(()-> new IllegalStateException("no game found")).getGameId());
    }

    @Test
    public void testGetGameByIdSuccess() {
        userService.login(ADMIN_USER);
        Game game = gameService.getGame(10)
                .shouldHave(statusCode(200))
                .as(Game.class);
        Assertions.assertNotNull(game);
    }

    @Test
    public void testGetGameByIdNotFound() {
        userService.login(ADMIN_USER);
        gameService.getGame(99)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Game with this id not exist"));
    }

    @Test
    public void deleteBaseGame() {
        userService.login(ADMIN_USER);
        gameService.deleteGame(10)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Cant delete game from base users"));
    }

    @Test
    public void deleteGameSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        List<Game> gamesBefore = gameService.getGames().asList(Game.class);
        Game game = gameService.addRandomGame(false).as("register_data", Game.class);

        List<Game> gamesAfterNewGame = gameService.getGames().asList(Game.class);
        Assertions.assertNotEquals(gamesBefore.size(), gamesAfterNewGame.size(), "Game not added");

        gameService.deleteGame(game.getGameId())
                .shouldHave(statusCode(200))
                .shouldHave(hasMessage("Game successfully deleted"));

        List<Game> gamesAfterDeleteGame = gameService.getGames().asList(Game.class);

        Assertions.assertEquals(gamesBefore.size(), gamesAfterDeleteGame.size(), "Game not deleted");
        userService.deleteAuthedUser();
    }

    @Test
    public void deleteDlcGameNotFound() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);
        gameService.deleteGame(-1)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Game with this id not exist"));
        userService.deleteAuthedUser();
    }

    @Test
    public void deleteDlcIfListNull() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);

        gameService.deleteListDlc(game.getGameId(), new ArrayList<>())
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("List with DLC to delete cant be empty or null"));
        userService.deleteAuthedUser();
    }

    @Test
    public void deleteDlcSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        List<DLC> toDeleteDlc = Collections.singletonList(game.getDlcs().get(0));
        gameService.deleteListDlc(game.getGameId(), toDeleteDlc)
                .shouldHave(statusCode(200))
                .shouldHave(hasMessage("Game DLC successfully deleted"));

        Game deletedDlc = gameService.getGame(game.getGameId()).as(Game.class);

        Assertions.assertNotEquals(game.getDlcs().size(), deletedDlc.getDlcs().size());
        userService.deleteAuthedUser();
    }

    @Test
    public void updateDlcInfoWithoutDlc() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        gameService.updateListDlc(game.getGameId(), new ArrayList<>())
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Empty body with list of dlc to modify"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateDlcInfoSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        DLC firstDlcToUpdate = game.getDlcs().get(0);
        firstDlcToUpdate.setDlcName("new value dlc");
        List<DLC> dlcList = Collections.singletonList(firstDlcToUpdate);

        gameService.updateListDlc(game.getGameId(), dlcList)
                .shouldHave(statusCode(200))
                .shouldHave(hasMessage("DlC successfully changed"));


        Game gameUpdated = gameService.getGame(game.getGameId()).as(Game.class);

        Assertions.assertNotEquals(game.getDlcs().get(0), gameUpdated.getDlcs().get(0));
        userService.deleteAuthedUser();
    }

    @Test
    public void addFreeGameWithPriceError() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = Utils.generateRandomGame(true);
        game.setIsFree(true);
        game.setPrice(20.0);

        gameService.addGame(game)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Free DLC or Game cant have price more than 0.0$"));
        userService.deleteAuthedUser();
    }

    @Test
    public void addFreeGameWithDlcPriceError() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = Utils.generateRandomGame(true);
        game.setIsFree(true);
        game.setPrice(0.0);
        game.getDlcs().get(0).setIsDlcFree(true);
        game.getDlcs().get(0).setPrice(20.0);

        gameService.addGame(game)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Free DLC or Game cant have price more than 0.0$"));
        userService.deleteAuthedUser();
    }

    @Test
    public void addGameSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        gameService.addRandomGame(true)
                .shouldHave(statusCode(201))
                .shouldHave(hasMessage("Game created"));
       // userService.deleteAuthedUser();
    }

    @Test
    public void updateGameIdFieldError() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("gameId", 10);
        gameService.updateField(game.getGameId(), updField)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Cannot edit ID field"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateNonExistFieldError() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("fakeField", 10);

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Cannot find field"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateFieldWithIncorrectType() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("title", 10);

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Cannot set new value because field has incorrect type"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateFieldWithSameValue() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("title", game.getTitle());

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("New field value is same as before"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateFieldSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("title", "new title");

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(statusCode(200))
                .shouldHave(hasMessage("New value edited successfully on field title"));
        userService.deleteAuthedUser();
    }

}
