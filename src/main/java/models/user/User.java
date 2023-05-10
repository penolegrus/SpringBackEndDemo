package models.user;


import models.game.Game;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String pass;
    @ElementCollection(targetClass = Game.class)
    @OneToMany(cascade = CascadeType.ALL)
    private List<Game> games;

    public User() {
    }

    public User(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public User(String login, String pass, List<Game> games) {
        this.login = login;
        this.pass = pass;
        this.games = games;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
