package app;

import models.User;
import models.game.DLC;
import models.game.Game;

import java.util.List;

public class UserGameService {
    private User user;

    public UserGameService(User user) {
        this.user = user;
    }

    public boolean isLimitForGames(){
        return user.getGames().size() <= 20;
    }

    public Game findGameById(Integer id) {
        return user.getGames().stream().filter(x -> x.getGameId().equals(id)).findFirst().orElse(null);
    }

    public void deleteGame(Integer gameId) {
        Game game = findGameById(gameId);
        user.getGames().remove(game);
    }

    public Game deleteDlc(Integer gameId, List<DLC> dlcList) {
        Game game = findGameById(gameId);
        user.getGames().remove(game);
        for (DLC dlc : dlcList) {
            game.getDlcs().removeIf(x -> x.getDlcName().equals(dlc.getDlcName()));
        }
        user.getGames().add(game);
        return game;
    }

    public boolean isPricesAndIsFreeCorrect(Game game) {
        if (game.getIsFree()) {
            if (!game.getPrice().equals(0.0)) {
                return false;
            }
        }

        List<DLC> dlcs = game.getDlcs();
        for (DLC dlc : dlcs) {
            if (dlc.getIsDlcFree()) {
                if (!dlc.getPrice().equals(0.0)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Game addGame(Game game) {
        user.getGames().add(game);
        return game;
    }

    public void updateGame(Game game) {
        Game old = findGameById(game.getGameId());
        user.getGames().remove(old);
        user.getGames().add(game);
    }
}
