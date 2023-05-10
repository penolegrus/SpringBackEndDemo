package service;

import db_models.User;
import db_models.game.DLC;
import db_models.game.Game;
import dto.SuccessGameResponse;
import jwt.config.JwtTokenUtil;
import models.InfoMessage;
import models.Message;
import models.game.UpdField;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repo.GameRepository;
import repo.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class GameService extends JwtService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, GameRepository gameRepository, UserRepository userRepository1) {
        super(userRepository, jwtTokenUtil);
        this.gameRepository = gameRepository;
        this.userRepository = userRepository1;
    }

    public ResponseEntity<List<Game>> getGames(String authHeader) {
        User user = getUserFromJwt(authHeader);
        return ResponseEntity.status(200).body(user.getGames());
    }

    public ResponseEntity<?> getGame(Long gameId, String authHeader) {
        User user = getUserFromJwt(authHeader);
        Game game = gameRepository.getGameByUserId(user.getId(), gameId);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }
        return ResponseEntity.status(200).body(game);
    }

    public ResponseEntity<InfoMessage> deleteGame(Long gameId, String authHeader) {
        User user = getUserFromJwt(authHeader);
        Game game = gameRepository.getGameByUserId(user.getId(), gameId);
        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }
        if (isBaseUser(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete game from base users"));
        }

        user.getGames().removeIf(x->x.getGameId() == game.getGameId());
        userRepository.save(user);
        userRepository.flush();

        gameRepository.deleteCascadeGameById(game.getGameId());
        gameRepository.flush();

        return ResponseEntity.status(200).body(new InfoMessage("success", "Game successfully deleted"));
    }

    public ResponseEntity<InfoMessage> deleteDlc(Long gameId, List<DLC> dls, String authHeader) {
        User user = getUserFromJwt(authHeader);
        Game game = gameRepository.getGameByUserId(user.getId(), gameId);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }

        if (isBaseUser(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete game from base users"));
        }

        if (dls == null || dls.isEmpty()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "List with DLC to delete cant be empty or null"));
        }

        for (DLC dlc : dls) {
            game.getDlcs().removeIf(x -> x.getDlcName().equals(dlc.getDlcName()));
        }

        gameRepository.save(game);
        gameRepository.flush();

        return ResponseEntity.status(200).body(new InfoMessage("success", "Game DLC successfully deleted"));
    }

    public ResponseEntity<?> addGame(Game newGame, String authHeader) {
        User user = getUserFromJwt(authHeader);

        if (!isPricesAndIsFreeCorrect(newGame)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Free DLC or Game cant have price more than 0.0$"));
        }

        if (isLimitForGames(authHeader)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Limit of games, user can have only 20 games"));
        }

        user.getGames().add(newGame);
        userRepository.save(user);
        userRepository.flush();

        user = getUserFromJwt(authHeader);
        Game freshGame = user.getGames().stream().reduce((first,second)->second).get();

        return ResponseEntity.status(201).body(new SuccessGameResponse(freshGame, new Message("success", "Game created")));
    }

    public ResponseEntity<InfoMessage> updateGameField(Long gameId, UpdField updField, String authHeader) {
        User user = getUserFromJwt(authHeader);
        Game oldGame = gameRepository.getGameByUserId(user.getId(), gameId);

        if (oldGame == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }

        if (isBaseUser(user)) {
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

        gameRepository.save(updated);
        gameRepository.flush();

        return ResponseEntity.status(200).body(new InfoMessage("success", "New value edited successfully on field " + updField.getFieldName()));
    }

    public ResponseEntity<InfoMessage> updateGameDlcInfo(Long gameId, List<DLC> dlcs, String authHeader) {
        User user = getUserFromJwt(authHeader);
        Game game = gameRepository.getGameByUserId(user.getId(), gameId);

        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }

        if (isBaseUser(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant update dlc game from base users"));
        }

        if (dlcs == null || dlcs.isEmpty()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Empty body with list of dlc to modify"));
        }

        List<DLC> dlsUpdated = game.getDlcs();
        dlsUpdated.addAll(dlcs);

        game.setDlcs(new ArrayList<>(new HashSet<>(dlsUpdated)));

        gameRepository.save(game);
        gameRepository.flush();
        return ResponseEntity.status(200).body(new InfoMessage("success", "DlC successfully changed"));
    }


    private boolean isPricesAndIsFreeCorrect(Game game) {
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

    private boolean isLimitForGames(String authHeader) {
        User user = getUserFromJwt(authHeader);
        return user.getGames().size() > 20;
    }
}
