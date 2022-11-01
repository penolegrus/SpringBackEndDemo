package services;

import assections.AssertableResponse;
import helpers.Utils;
import models.game.DLC;
import models.game.Game;
import models.game.UpdField;

import java.util.List;

public class GameService extends WebService {

    public GameService(String basePath) {
        super(basePath);
    }

    public AssertableResponse getGames() {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).get("games").then());
    }

    public AssertableResponse getGame(int id) {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).get("games/" + id).then());
    }

    public AssertableResponse deleteGame(int id) {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).delete("games/" + id).then());
    }

    public AssertableResponse addRandomGame(boolean withDlc) {
        Game game = withDlc ? Utils.generateRandomGame(true) : Utils.generateRandomGame(false);
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).body(game).post("games").then());
    }

    public AssertableResponse deleteListDlc(int id, List<DLC> dlcList) {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt)
                .body(dlcList).delete(String.format("games/%d/dlc", id)).then());
    }

    public AssertableResponse updateListDlc(int id, List<DLC> dlcList) {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).body(dlcList).put("games/" + id).then());
    }

    public AssertableResponse addGame(Game game) {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).body(game).post("games").then());
    }

    public AssertableResponse updateField(int id, UpdField updField) {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).body(updField).put(String.format("games/%d/updateField", id)).then());
    }
}
