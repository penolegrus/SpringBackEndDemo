package app;

import helpers.Utils;
import models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UserDataBase {

    public static List<User> baseUsers = new ArrayList<>();
    public static List<User> users = new ArrayList<>();

    static {
        baseUsers.add(new User("admin", "admin", 1, Collections.singletonList(Utils.generateRandomGame(true, 1))));
        baseUsers.add(new User("demo", "password", 2, Collections.singletonList(Utils.generateRandomGame(false, 2))));
        baseUsers.add(new User("threadqa", "iloveqa", 3, Collections.singletonList(Utils.generateRandomGame(true, 3))));
        users.addAll(baseUsers);
    }

    public static boolean isLoginExists(String login) {
        return users.stream().anyMatch(x -> x.getLogin().equalsIgnoreCase(login));
    }

    public boolean isUserInBaseUsers(Integer id) {
        return baseUsers.stream().anyMatch(x -> x.getId().equals(id));
    }

    public static boolean isUserInBaseUsers(User user) {
        return baseUsers.contains(user);
    }

    public static void updateUser(User oldDataUser, String password) {
        User user = getUser(oldDataUser.getId());
        user.setPass(password);
    }

    public static User getUser(Integer id) {
        return users.stream().filter(x -> x.getId().equals(id)).findFirst()
                .orElse(null);
    }

    public static User getUser(String userName) {
        return users.stream().filter(x -> x.getLogin().equals(userName)).findFirst()
                .orElse(null);
    }

    public boolean isUserExist(Integer id) {
        return users.stream().anyMatch(x -> x.getId().equals(id));
    }

    public boolean deleteUser(Integer id) {
        User user = users.stream().filter(x -> x.getId().equals(id)).findFirst().get();
        users.remove(user);
        return true;
    }

    public static List<User> getAllUsers() {
        return users;
    }

    public void updateDataBase(List<User> newUserList) {
        users = newUserList;
    }

    public static void removeLastUsers(int count) {
        for (int i = 0; i < count; i++) {
            users.remove(users.size() - 1);
        }
    }

    public static User createUser(User user) {
        users.add(user);
        return user;
    }
}
