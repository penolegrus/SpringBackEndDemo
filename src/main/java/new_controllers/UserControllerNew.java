package new_controllers;

import models.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import models.messages.InfoMessage;
import models.user.ChangeUserPass;
import models.user.RegisterUserResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllerNew {
    private final UserService userService;

    @PostMapping(path = "/api/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Регистрация нового пользователя")
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
    public ResponseEntity<?> addUser(@RequestBody User userDTO) {
        return userService.signUp(userDTO);
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
    public ResponseEntity<InfoMessage> updateUserPassword(@Parameter(name = "Новый пароль пользователя")
                                                          @RequestBody ChangeUserPass passwordJson,
                                                          @Parameter(name = "Authorization")
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return userService.updateUserPassword(passwordJson, authHeader);
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
        return userService.getUser(authHeader);
    }

    @Operation(summary = "Показывает логины всех существующих пользователей")
    @GetMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список с логины всех существующих пользователей",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String[].class))),
    })
    @ResponseBody
    public ResponseEntity<List<String>> getAllUserNames() {
        return userService.getAllUsers();
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
        return userService.deleteUser(authHeader);
    }

}
