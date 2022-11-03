package tests;

import assections.Conditions;
import core.TestBase;
import extensions.listeners.AllureLoggingListener;
import extensions.parameter_extension.RandomUser;
import extensions.parameter_extension.RandomUserParameterExtension;
import helpers.Utils;
import models.user.User;
import models.game.DLC;
import models.game.Game;
import models.game.UpdField;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import services.GameService;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static testdata.TestData.ADMIN_USER;

@ExtendWith(AllureLoggingListener.class)
public class GamesApiTest extends TestBase {

    private final GameService gameService = new GameService();

    private Game addRandomGame(boolean witDlc) {
        return gameService.addRandomGame(witDlc).as("register_data", Game.class);
    }

    @Test
    public void testGetGameByIdSuccess() {
        userService.login(ADMIN_USER);
        Game game = gameService.getGame(1)
                .shouldHave(Conditions.statusCode(200))
                .as(Game.class);
        Assertions.assertNotNull(game);
    }

    @Test
    public void testGetGameByIdNotFound() {
        userService.login(ADMIN_USER);
        gameService.getGame(99)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Game with this id not exist"));
    }

    @Test
    public void deleteBaseGame() {
        userService.login(ADMIN_USER);
        gameService.deleteGame(1)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Cant delete game from base users"));
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
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.hasMessage("Game successfully deleted"));

        List<Game> gamesAfterDeleteGame = gameService.getGames().asList(Game.class);

        Assertions.assertEquals(gamesBefore.size(), gamesAfterDeleteGame.size(), "Game not deleted");
        userService.deleteAuthedUser();
    }

    @Test
    public void deleteDlcGameNotFound() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);
        gameService.deleteGame(-1)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Game with this id not exist"));
        userService.deleteAuthedUser();
    }

    @Test
    public void deleteDlcIfListNull() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);

        gameService.deleteListDlc(game.getGameId(), new ArrayList<>())
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("List with DLC to delete cant be empty or null"));
        userService.deleteAuthedUser();
    }

    @Test
    public void deleteDlcSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        List<DLC> toDeleteDlc = Collections.singletonList(game.getDlcs().get(0));
        gameService.deleteListDlc(game.getGameId(), toDeleteDlc)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.hasMessage("Game DLC successfully deleted"));

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
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Empty body with list of dlc to modify"));
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
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.hasMessage("DlC successfully changed"));


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
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Free DLC or Game cant have price more than 0.0$"));
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
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Free DLC or Game cant have price more than 0.0$"));
        userService.deleteAuthedUser();
    }

    @Test
    public void addGameSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        gameService.addRandomGame(true)
                .shouldHave(Conditions.statusCode(201))
                .shouldHave(Conditions.hasMessage("Game created"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateGameIdFieldError() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("gameId", 10);
        gameService.updateField(game.getGameId(), updField)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Cannot edit ID field"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateNonExistFieldError() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("fakeField", 10);

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Cannot find field"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateFieldWithIncorrectType() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("title", 10);

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Cannot set new value because field has incorrect type"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateFieldWithSameValue() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("title", game.getTitle());

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("New field value is same as before"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateFieldSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        Game game = addRandomGame(true);
        UpdField updField = new UpdField("title", "new title");

        gameService.updateField(game.getGameId(), updField)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.hasMessage("New value edited successfully on field title"));
        userService.deleteAuthedUser();
    }

}
