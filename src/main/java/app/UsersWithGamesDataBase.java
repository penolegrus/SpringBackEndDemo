package app;

import helpers.Utils;
import models.UserWithGames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UsersWithGamesDataBase {

    public List<UserWithGames> users = new ArrayList<>();

    public UsersWithGamesDataBase() {
        users.add(new UserWithGames(1, Collections.singletonList(Utils.generateRandomGame(true, 1))));
        users.add(new UserWithGames(2, Collections.singletonList(Utils.generateRandomGame(false, 2))));
        users.add(new UserWithGames(3, Collections.singletonList(Utils.generateRandomGame(true, 3))));
        users.add(new UserWithGames(4, Collections.singletonList(Utils.generateRandomGame(false, 4))));
    }
}
