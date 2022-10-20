package models;

import models.game.Game;

import java.util.List;

public class UserWithGames {
    private Integer id;
    private List<Game> games;

    public UserWithGames(Integer id, List<Game> games) {
        this.id = id;
        this.games = games;
    }

    public UserWithGames() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "UserWithGames{" +
                "id=" + id +
                ", games=" + games +
                '}';
    }
}
