package app;

import jwt.config.JwtTokenUtil;
import models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        String tokenAdmin = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY2NjI4MjAyMSwiaWF0IjoxNjY2MjY0MDIxfQ.yFvyhIZmI-M0p6CxIf_LFeLuHB3hafEBOzlF8cB5QI4xC3slFBNoN-eCb_fnmmXecAnpCuOU3kFvdYPqwEwumQ";
        //получаем юзера по айдишке
        User user = UserDataBase.getUser(1);

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String decoded = jwtTokenUtil.getUsernameFromToken(tokenAdmin);

        System.out.println(tokenAdmin);
        System.out.println(decoded);

        //если актуальный токен не сходится с тем, что принадлежит юзеру, то не показываем данные
        if(!decoded.equals(user.getLogin())){
            System.out.println("token is incorrect");
        } else {
            System.out.println(UsersWithGamesDataBase.users.get(1).toString());
        }
    }


}
