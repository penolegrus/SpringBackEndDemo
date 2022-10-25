package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.game.Game;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String pass;
    private Integer id;
    private List<Game> games;

    public User(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }
    public User(String login) {
        this.login = login;
    }


    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
