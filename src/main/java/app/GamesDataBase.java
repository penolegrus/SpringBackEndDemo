package app;

import helpers.Utils;
import models.game.DLC;
import models.game.Game;

import java.lang.reflect.Field;
import java.util.*;

public class GamesDataBase {
    public static List<Game> baseGames = new ArrayList<>();
    public static List<Game> games = new ArrayList<>();

    static {
        baseGames.add(Utils.generateRandomGame(true,1));
        baseGames.add(Utils.generateRandomGame(false,2));
        baseGames.add(Utils.generateRandomGame(true,3));
        baseGames.add(Utils.generateRandomGame(false,4));
        games.addAll(baseGames);
    }

    public boolean isGameInBaseGames(Game game) {
        return baseGames.contains(game);
    }
    public boolean isGameInBaseGames(Integer id) {
        return baseGames.stream().anyMatch(x -> x.getGameId().equals(id));
    }

    public Game findGameById(Integer id){
        return games.stream().filter(x->x.getGameId().equals(id)).findFirst().orElse(null);
    }

    public Game addGame(Game game) {
        games.add(game);
        return game;
    }

    public void deleteGame(Integer gameId) {
        Game game = findGameById(gameId);
        games.remove(game);
    }

    public Game deleteDlc(Integer gameId, List<DLC> dlcList) {
        Game game = findGameById(gameId);
        games.remove(game);
        for (DLC dlc : dlcList) {
            game.getDlcs().removeIf(x->x.getDlcName().equals(dlc.getDlcName()));
        }
        games.add(game);
        return game;
    }

    public List<Game> getAllGames() {
        return games;
    }
    public boolean isGameExist(Integer gameId) {
        return games.stream().anyMatch(x->x.getGameId().equals(gameId));
    }

    public boolean isGameHasAllFilledFields(Game game) {
        Field[] fields = game.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(game);
                if (value instanceof DLC) {
                    continue;
                }
                if (value == null) {
                    return false;
                }
            }
        } catch (IllegalAccessException ignored) {
        }
        return true;
    }

    public boolean isPricesAndIsFreeCorrect(Game game) {
        if (game.getFree()) {
            if (!game.getPrice().equals(0.0)) {
                return false;
            }
        }

        List<DLC> dlcs = game.getDlcs();
        for (DLC dlc : dlcs) {
            if (dlc.getDlcFree()) {
                if (!dlc.getPrice().equals(0.0)) {
                    return false;
                }
            }
        }
        return true;

    }

    public void updateGame(Game game) {
        Game old = findGameById(game.getGameId());
        games.remove(old);
        games.add(game);
    }

    public void updateDataBase(List<Game> gamesNew) {
        games = gamesNew;
    }
}
