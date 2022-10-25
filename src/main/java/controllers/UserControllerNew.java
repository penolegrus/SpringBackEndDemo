package controllers;

import app.UserDataBase;
import helpers.Utils;
import jwt.config.JwtTokenUtil;
import models.*;
import models.game.Game;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static app.UserDataBase.*;
import static helpers.Constants.USERS_API_URL;

@RestController
public class UserControllerNew {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private User getUserFromJwt(String jwt) {
        jwt = jwt.replace("Bearer ", "");
        String getUserLoginFromToken = jwtTokenUtil.getUsernameFromToken(jwt);
        return UserDataBase.getUser(getUserLoginFromToken);
    }


    @PutMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateUserPassword(@RequestBody ChangeUserPass passwordJson,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        if (passwordJson.getPassword() == null || passwordJson.getPassword().isEmpty()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Body has no password parameter"));
        }

        User oldUser = getUserFromJwt(authHeader);

        if (UserDataBase.isUserInBaseUsers(oldUser)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant update base users"));
        }

        UserDataBase.updateUser(oldUser, passwordJson.getPassword());

        return ResponseEntity.status(200).body(new InfoMessage("success", "User password successfully changed"));
    }

    @GetMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // получаем юзера по логину
        User user = getUserFromJwt(authHeader);
        return ResponseEntity.status(200).body(user);
    }

    @GetMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAllUserNames() {

        List<String> names = UserDataBase.getAllUsers().stream().map(User::getLogin).collect(Collectors.toList());
        UserLogins usersOnlyNames = new UserLogins(names);

        return ResponseEntity.status(200).body(usersOnlyNames);
    }

    @GetMapping(path = "/api/user/games", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getGames(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);
        return ResponseEntity.status(200).body(user.getGames());
    }

    @GetMapping(path = "/api/user/games/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getGames(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        Game game = user.getGames().stream()
                .filter(x -> x.getGameId().equals(id))
                .findFirst().orElse(null);
        if (game == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Game with this id not exist"));
        }
        return ResponseEntity.status(200).body(game);
    }


    @PostMapping(path = "/api/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerPost(@RequestBody User user) {

        int limitToDelete = 200;
        if (getAllUsers().size() > limitToDelete) {
            removeLastUsers(100);
        }
        JSONObject response = new JSONObject();

        if (user.getLogin() == null || user.getPass() == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Missing login or password"));
        }

        if (isLoginExists(user.getLogin())) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Login already exist"));
        }

        user.setId(Utils.getRandomInt());
        response.put("info", new InfoMessage("success", "User created"));
        response.put("register_data", user);

        createUser(user);
        return ResponseEntity.status(201).body(response);
    }


    @PostMapping(path = USERS_API_URL + "/addUsers/{count}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addUsersToDataBase(@PathVariable Integer count) {
        if (count > 250) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "You can add less than 250 random users"));
        }

        for (int i = 0; i < count; i++) {

            List<Game> games = new ArrayList<>();
            for (int k = 0; k <= Utils.random.nextInt(3); k++) {
                games.add(Utils.generateRandomGame());
            }

            createUser(new User("testLogin" + i, "testPassword" + i, Utils.getRandomInt(), games));
        }
        return ResponseEntity.status(200)
                .body(new InfoMessage("success",
                        count + " users added to database. Users count - " + getAllUsers().size()));
    }
}
