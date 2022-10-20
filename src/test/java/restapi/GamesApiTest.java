package restapi;

import helpers.Utils;
import models.game.DLC;
import models.game.Game;
import models.game.UpdField;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static helpers.Constants.GAME_API_URL;

public class GamesApiTest extends BaseApiTest {


    @BeforeAll
    public static void setBasePath() {
        reqBuilder.setBasePath(GAME_API_URL);
    }

    private List<Game> getAllGames() {
        return rest.get().asList(Game.class);
    }

    @Test
    public void testGetGameByIdSuccess() {
        Game game = rest.get("/1")
                .hasStatusCode(200)
                .as(Game.class);
        Assertions.assertNotNull(game);
    }

    @Test
    public void testGetGameByIdNotFound() {
        rest.get("/5")
                .hasStatusCode(400)
                .hasMessage("Game not found");
    }

    @Test
    public void deleteBaseGame() {
        rest.delete("/1")
                .hasStatusCode(400)
                .hasMessage("Cant delete base games");
    }

    @Test
    public void deleteGameSuccess() {
        List<Game> gamesBefore = getAllGames();
        Game game = addGame(false);

        List<Game> gamesAfterNewGame = getAllGames();

        Assertions.assertNotEquals(gamesBefore.size(), gamesAfterNewGame.size(), "Game not added");

        rest.delete("/" + game.getGameId())
                .hasStatusCode(200)
                .hasMessage("Game successfully deleted");

        List<Game> gamesAfterDeleteGame = getAllGames();

        Assertions.assertEquals(gamesBefore.size(), gamesAfterDeleteGame.size(), "Game not deleted");
    }

    @Test
    public void deleteDlcGameNotFound() {
        rest.delete("/-1").hasStatusCode(400).hasMessage("Game not found to delete");
    }

    @Test
    public void deleteDlcBaseGame() {
        rest.delete("/1").hasStatusCode(400).hasMessage("Cant delete base games");
    }

    @Test
    public void deleteDlcIfListNull() {
        Game game = addGame(true);
        String dlcEndPoint = String.format("/%d/dlc", game.getGameId());
        List<DLC> toDeleteDlc = new ArrayList<>();

        rest.delete(dlcEndPoint).hasStatusCode(400)
                .hasMessage("List with DLC to delete cant be empty or null");

        rest.delete(dlcEndPoint, toDeleteDlc).hasStatusCode(400)
                .hasMessage("List with DLC to delete cant be empty or null");
    }

    @Test
    public void deleteDlcSuccess() {
        Game game = addGame(true);
        String dlcEndPoint = String.format("/%d/dlc", game.getGameId());
        List<DLC> toDeleteDlc = Collections.singletonList(game.getDlcs().get(0));

        rest.delete(dlcEndPoint, toDeleteDlc).hasStatusCode(200).hasMessage("Game DLC successfully deleted");

        Game deletedDlc = getGame(game.getGameId());

        Assertions.assertNotEquals(game.getDlcs().size(), deletedDlc.getDlcs().size());
    }

    @Test
    public void updateDlcInfoErrorGameNotFound() {
        rest.put("-1").hasStatusCode(400).hasMessage("Game not found to modify");
    }

    @Test
    public void updateDlcInfoBaseGame() {
        rest.put("1").hasStatusCode(400).hasMessage("Cant modify base games");
    }

    @Test
    public void updateDlcInfoWithoutDlc() {
        Game game = addGame(true);
        rest.put(String.valueOf(game.getGameId()))
                .hasStatusCode(400)
                .hasMessage("Empty body with list of dlc to modify");
    }

    @Test
    public void updateDlcInfoSuccess() {
        Game game = addGame(true);
        DLC firstDlcToUpdate = game.getDlcs().get(0);
        firstDlcToUpdate.setDlcName("new value dlc");
        List<DLC> dlcList = Collections.singletonList(firstDlcToUpdate);

        rest.put(String.valueOf(game.getGameId()), dlcList).hasStatusCode(200)
                .hasMessage("DlC successfully changed");

        Game gameUpdated = getGame(game.getGameId());

        Assertions.assertNotEquals(game.getDlcs().get(0), gameUpdated.getDlcs().get(0));
    }

    @Test
    public void addFreeGameWithPriceError() {
        Game game = Utils.generateRandomGame(true);
        game.setFree(true);
        game.setPrice(20.0);
        rest.post(game).hasStatusCode(400).hasMessage("Free DLC or Game cant have price more than 0.0$");
    }

    @Test
    public void addFreeGameWithDlcPriceError() {
        Game game = Utils.generateRandomGame(true);
        game.setFree(true);
        game.setPrice(0.0);
        game.getDlcs().get(0).setDlcFree(true);
        game.getDlcs().get(0).setPrice(20.0);
        rest.post(game).hasStatusCode(400).hasMessage("Free DLC or Game cant have price more than 0.0$");
    }

    @Test
    public void addGameSuccess() {
        Game game = Utils.generateRandomGame(true);
        rest.post(game).hasStatusCode(201).hasMessage("Game created");
    }

    @Test
    public void updateGameIdFieldError() {
        Game game = addGame(true);
        String endpoint = String.format("/%s/updateField", game.getGameId());
        UpdField updField = new UpdField("gameId", 10);

        rest.put(endpoint, updField).hasStatusCode(400).hasMessage("Cannot edit ID field");
    }

    @Test
    public void updateNonExistFieldError() {
        Game game = addGame(true);
        String endpoint = String.format("/%s/updateField", game.getGameId());
        UpdField updField = new UpdField("fakeField", 10);

        rest.put(endpoint, updField).hasStatusCode(400).hasMessage("Cannot find field");
    }

    @Test
    public void updateFieldWithIncorrectType() {
        Game game = addGame(true);
        String endpoint = String.format("/%s/updateField", game.getGameId());
        UpdField updField = new UpdField("title", 10);

        rest.put(endpoint, updField).hasStatusCode(400).hasMessage("Cannot set new value because field has incorrect type");
    }

    @Test
    public void updateFieldWithSameValue() {
        Game game = addGame(true);
        String endpoint = String.format("/%s/updateField", game.getGameId());
        UpdField updField = new UpdField("title", game.getTitle());
        rest.put(endpoint, updField).hasStatusCode(400).hasMessage("New field value is same as before");
    }

    @Test
    public void updateFieldSuccess() {
        Game game = addGame(true);
        String endpoint = String.format("/%s/updateField", game.getGameId());
        UpdField updField = new UpdField("title", "new title");
        rest.put(endpoint, updField).hasStatusCode(200).hasMessage("New value edited successfully on field title");
    }

    private Game addGame(boolean withDlc) {
        Game game = withDlc ? Utils.generateRandomGame(true) : Utils.generateRandomGame(false);
        return rest.post(game).as("register_data", Game.class);
    }

    private Game getGame(int gameId) {
        return rest.get("/" + gameId).as(Game.class);
    }
}
