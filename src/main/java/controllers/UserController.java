package controllers;

import app.UserDataBase;
import helpers.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jwt.config.JwtTokenUtil;
import models.*;
import models.user.ChangeUserPass;
import models.user.RegisterUserResponse;
import models.user.User;
import models.user.UserLogins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static app.UserDataBase.*;

@RestController
public class UserController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private User getUserFromJwt(String jwt) {
        jwt = jwt.replace("Bearer ", "");
        String getUserLoginFromToken = jwtTokenUtil.getUsernameFromToken(jwt);
        return UserDataBase.getUser(getUserLoginFromToken);
    }

    @Operation(summary = "Обновление пароля у пользователя")
    @PutMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пароль обновлен успешно",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пароль не обновлен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @ResponseBody
    public ResponseEntity<InfoMessage> updateUserPassword(@Parameter(name = "Новый пароль пользователя") @RequestBody ChangeUserPass passwordJson,
                                                @Parameter(name = "JWT токен") @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

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

    @Operation(summary = "Получение информации о пользователе")
    @GetMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация о пользователе",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class))),
            })
    @ResponseBody
    public ResponseEntity<User> getUser(@Parameter(description = "JWT токен")
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);
        return ResponseEntity.status(200).body(user);
    }

    @Operation(summary = "Показывает логины всех существующих пользователей")
    @GetMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список с логины всех существующих пользователей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserLogins.class))),
             })
    @ResponseBody
    public ResponseEntity<UserLogins> getAllUserNames() {
        List<String> names = UserDataBase.getAllUsers().stream().map(User::getLogin).collect(Collectors.toList());
        UserLogins usersOnlyNames = new UserLogins(names);

        return ResponseEntity.status(200).body(usersOnlyNames);
    }

    @Operation(summary = "Удаляет пользователя из Базы данных")
    @DeleteMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно удален",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Нельзя удалить начальных пользователей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @ResponseBody
    public ResponseEntity<InfoMessage> deleteUserFromDb(@Parameter(description = "JWT токен")
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        User user = getUserFromJwt(authHeader);

        if (isUserInBaseUsers(user.getId())) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete base users"));
        }

        deleteUser(user.getId());
        return ResponseEntity.status(200).body(new InfoMessage("success", "User successfully deleted"));
    }

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping(path = "/api/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь зарегистрирован",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegisterUserResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка регистрации, неверные поля",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InfoMessage.class)))
    })
    @ResponseBody
    public ResponseEntity<?> registerPost(@Parameter(description = "Json схема пользователя, можно без полей id и games регистрировать")
                                              @RequestBody User user) {

        int limitToDelete = 200;
        if (getAllUsers().size() > limitToDelete) {
            removeLastUsers(100);
        }

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
        createUser(user);
        return ResponseEntity.status(201).body(new RegisterUserResponse(user, new Message("success", "User created")));
    }
}
