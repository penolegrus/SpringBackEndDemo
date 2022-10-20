package controllers;

import app.UserDataBase;
import app.UsersWithGamesDataBase;
import helpers.InfoResponse;
import jwt.config.JwtTokenUtil;
import models.User;
import models.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static helpers.Constants.GAME_API_URL;
@RestController
public class UserWithGamesController {
    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @GetMapping(path = "/api/usersWithGame/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getGame(@PathVariable Integer id, HttpServletResponse response) {
        //колхозный вариант просмотра игр у пользователя под определенным токеном авторизации

        //получаем юзера по айдишке
        User user = UserDataBase.getUser(id);

        //падает на этом моменте, потому что сервлет пустой прилетает почему то
        String jwt = response.getHeader("Authorization").replace("Bearer ", "");

        //Из токена авторизации достаем логин
        String getUserLoginFromToken = jwtTokenUtil.getUsernameFromToken(jwt);

        //если токен не принадлежит юзеру
        if(!getUserLoginFromToken.equals(user.getLogin())){
            return "token is incorrect";
        }
       return UsersWithGamesDataBase.users.get(id).toString();
    }
}
