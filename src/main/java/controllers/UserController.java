package controllers;

import app.UserDataBase;
import helpers.Utils;
import jwt.config.JwtTokenUtil;
import models.*;
import models.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static app.UserDataBase.*;
import static helpers.Constants.USERS_API_URL;

@RestController
public class UserController {
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

    @DeleteMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteUserFromDb(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        if (isUserInBaseUsers(user.getId())) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete base users"));
        }

        deleteUser(user.getId());
        return ResponseEntity.status(200).body(new InfoMessage("success", "User successfully deleted"));
    }


    @PostMapping(path = "/api/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerPost(@RequestBody User user) {

        int limitToDelete = 200;
        if (getAllUsers().size() > limitToDelete) {
            removeLastUsers(100);
        }
        Map<String, Object> response = new HashMap<>();

        if (user.getLogin() == null || user.getPass() == null) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Missing login or password"));
        }

        if (isLoginExists(user.getLogin())) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Login already exist"));
        }

        if (user.getGames() == null) {
            user.setGames(new ArrayList<>());
        }

        user.setId(Utils.getRandomInt());
        response.put("info", new Message("success", "User created"));
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
