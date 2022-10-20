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

import javax.servlet.http.HttpServletResponse;

import static helpers.Constants.GAME_API_URL;

public class UserWithGamesController {
    private UsersWithGamesDataBase usersWithGamesDataBase = new UsersWithGamesDataBase();
    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @GetMapping(path = "/api/usersWithGame/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getGame(@PathVariable Integer id, HttpServletResponse response) {
        //колхозный вариант просмотра игр у пользователя под определенным токеном авторизации

        //получаем юзера по айдишке
        User user = UserDataBase.getUser(id);

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(user.getLogin());

        //генерируем токен для этого юзера
        String actualToken = jwtTokenUtil.generateToken(userDetails);

        //если актуальный токен не сходится с тем, что принадлежит юзеру, то не показываем данные
        if(!response.getHeader("Authorization").equals(actualToken)){
            return "token is incorrect";
        }
       return usersWithGamesDataBase.users.get(id).toString();
    }
}
