package restapi;

import helpers.Utils;
import models.user.User;
import models.game.DLC;
import models.game.Game;
import models.game.UpdField;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static helpers.Constants.REGISTER_API_URL;
import static restapi.utils.TestUsers.DEMO_USER;

public class GamesApiTest extends BaseApiTest {


    private List<Game> getAllGames() {
        return restService.get(token, "/api/user/games").asList(Game.class);
    }

    @Test
    public void testGetGameByIdSuccess() {
        auth(DEMO_USER);
        Game game = restService.get(token, "/api/user/games/2")
                .hasStatusCode(200)
                .as(Game.class);
        Assertions.assertNotNull(game);
    }

    @Test
    public void testGetGameByIdNotFound() {
        auth(DEMO_USER);
        restService.get(token, "/api/user/games/1")
                .hasStatusCode(400)
                .hasMessage("Game with this id not exist");
    }

    @Test
    public void deleteBaseGame() {
        auth(DEMO_USER);
        restService.delete(token, "/api/user/games/2")
                .hasStatusCode(400)
                .hasMessage("Cant delete game from base users");
    }

    @Test
    public void deleteGameSuccess() {
        createUserAndAuth();

        List<Game> gamesBefore = getAllGames();
        Game game = addGame(false);

        List<Game> gamesAfterNewGame = getAllGames();

        Assertions.assertNotEquals(gamesBefore.size(), gamesAfterNewGame.size(), "Game not added");

        restService.delete(token, "/api/user/games/" + game.getGameId())
                .hasStatusCode(200)
                .hasMessage("Game successfully deleted");

        List<Game> gamesAfterDeleteGame = getAllGames();

        Assertions.assertEquals(gamesBefore.size(), gamesAfterDeleteGame.size(), "Game not deleted");
    }

    @Test
    public void deleteDlcGameNotFound() {
        createUserAndAuth();
        restService.delete(token,"/api/user/games/-1").hasStatusCode(400).hasMessage("Game with this id not exist");
    }

    @Test
    public void deleteDlcIfListNull() {
        createUserAndAuth();

        Game game = addGame(true);
        String dlcEndPoint = String.format("/api/user/games/%d/dlc", game.getGameId());
        List<DLC> toDeleteDlc = new ArrayList<>();

        restService.delete(token, dlcEndPoint).hasStatusCode(400)
                .hasMessage("List with DLC to delete cant be empty or null");

        restService.delete(token, dlcEndPoint, toDeleteDlc).hasStatusCode(400)
                .hasMessage("List with DLC to delete cant be empty or null");
    }

    @Test
    public void deleteDlcSuccess() {
        createUserAndAuth();

        Game game = addGame(true);
        String dlcEndPoint = String.format("/api/user/games/%d/dlc", game.getGameId());
        List<DLC> toDeleteDlc = Collections.singletonList(game.getDlcs().get(0));

        restService.delete(token, dlcEndPoint, toDeleteDlc).hasStatusCode(200).hasMessage("Game DLC successfully deleted");

        Game deletedDlc = getGame(game.getGameId());

        Assertions.assertNotEquals(game.getDlcs().size(), deletedDlc.getDlcs().size());
    }

    @Test
    public void updateDlcInfoWithoutDlc() {
        createUserAndAuth();

        Game game = addGame(true);
        String endPoint = String.format("/api/user/games/%d", game.getGameId());
        restService.put(token, endPoint)
                .hasStatusCode(400)
                .hasMessage("Empty body with list of dlc to modify");
    }

    @Test
    public void updateDlcInfoSuccess() {
        createUserAndAuth();

        Game game = addGame(true);
        DLC firstDlcToUpdate = game.getDlcs().get(0);
        firstDlcToUpdate.setDlcName("new value dlc");
        List<DLC> dlcList = Collections.singletonList(firstDlcToUpdate);

        String endPoint = String.format("/api/user/games/%d", game.getGameId());

        restService.put(token, endPoint, dlcList)
                .hasStatusCode(200)
                .hasMessage("DlC successfully changed");

        Game gameUpdated = getGame(game.getGameId());

        Assertions.assertNotEquals(game.getDlcs().get(0), gameUpdated.getDlcs().get(0));
    }

    @Test
    public void addFreeGameWithPriceError() {
        createUserAndAuth();
        Game game = Utils.generateRandomGame(true);
        game.setIsFree(true);
        game.setPrice(20.0);
        restService.post(token, "/api/user/games", game)
                .hasStatusCode(400)
                .hasMessage("Free DLC or Game cant have price more than 0.0$");
    }

    @Test
    public void addFreeGameWithDlcPriceError() {
        createUserAndAuth();
        Game game = Utils.generateRandomGame(true);
        game.setIsFree(true);
        game.setPrice(0.0);
        game.getDlcs().get(0).setIsDlcFree(true);
        game.getDlcs().get(0).setPrice(20.0);
        restService.post(token, "/api/user/games", game)
                .hasStatusCode(400)
                .hasMessage("Free DLC or Game cant have price more than 0.0$");
    }

    @Test
    public void addGameSuccess() {
        createUserAndAuth();
        Game game = Utils.generateRandomGame(true);
        restService.post(token, "/api/user/games", game)
                .hasStatusCode(201)
                .hasMessage("Game created");
    }

    @Test
    public void updateGameIdFieldError() {
        createUserAndAuth();
        Game game = addGame(true);
        String endpoint = String.format("/api/user/games/%d/updateField", game.getGameId());
        UpdField updField = new UpdField("gameId", 10);

        restService.put(token, endpoint, updField)
                .hasStatusCode(400)
                .hasMessage("Cannot edit ID field");
    }

    @Test
    public void updateNonExistFieldError() {
        createUserAndAuth();

        Game game = addGame(true);
        String endpoint = String.format("/api/user/games/%d/updateField", game.getGameId());
        UpdField updField = new UpdField("fakeField", 10);

        restService.put(token, endpoint, updField)
                .hasStatusCode(400)
                .hasMessage("Cannot find field");
    }

    @Test
    public void updateFieldWithIncorrectType() {
        createUserAndAuth();

        Game game = addGame(true);
        String endpoint = String.format("/api/user/games/%d/updateField", game.getGameId());
        UpdField updField = new UpdField("title", 10);

        restService.put(token, endpoint, updField)
                .hasStatusCode(400)
                .hasMessage("Cannot set new value because field has incorrect type");
    }

    @Test
    public void updateFieldWithSameValue() {
        createUserAndAuth();

        Game game = addGame(true);
        String endpoint = String.format("/api/user/games/%d/updateField", game.getGameId());
        UpdField updField = new UpdField("title", game.getTitle());
        restService.put(token, endpoint, updField)
                .hasStatusCode(400)
                .hasMessage("New field value is same as before");
    }

    @Test
    public void updateFieldSuccess() {
        createUserAndAuth();

        Game game = addGame(true);
        String endpoint = String.format("/api/user/games/%d/updateField", game.getGameId());
        UpdField updField = new UpdField("title", "new title");
        restService.put(token, endpoint, updField)
                .hasStatusCode(200)
                .hasMessage("New value edited successfully on field title");
    }

    private Game addGame(boolean withDlc) {
        Game game = withDlc ? Utils.generateRandomGame(true) : Utils.generateRandomGame(false);
        return restService.post(token, "/api/user/games", game).as("register_data", Game.class);
    }

    private Game getGame(int gameId) {
        return restService.get(token, "/api/user/games/" + gameId).as(Game.class);
    }

    private User createUserAndAuth(){
        User user = new User("newLogin" + Utils.getRandomInt(), "oldPass");
        User created = restService.post(REGISTER_API_URL, user).as("register_data", User.class);
        auth(created);
        return created;
    }
}
