package dto;


import db_models.game.Game;

import java.util.List;

public class UserDTO {
    private String login;
    private String pass;
    private List<Game> games;

    public UserDTO(String login, String pass, List<Game> games) {
        this.login = login;
        this.pass = pass;
        this.games = games;
    }

    public UserDTO(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
