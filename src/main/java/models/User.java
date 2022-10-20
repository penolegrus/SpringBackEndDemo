package models;

import org.json.JSONObject;

import java.util.Objects;
import java.util.Random;

public class User {
    private String login;
    private String pass;

    private Integer id;

    public User(String login, String pass) {
        this.login = login;
        this.pass = pass;
        this.id = id;
    }

    public User(String login, String pass, Integer id) {
        this.login = login;
        this.pass = pass;
        this.id = id;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRandomId() {
        this.id = Math.abs(new Random().nextInt());
        ;
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

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(pass, user.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, pass);
    }
}
