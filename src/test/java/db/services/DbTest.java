package db.services;

import core.TestBase;
import db.JpaExtension;
import helpers.Utils;
import models.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

@ExtendWith(JpaExtension.class)
public class DbTest extends TestBase {
    private final UserDbService userDbService = new UserDbService();

    @Test
    public void userTest() {
        User user = userDbService.get(1);
        Assertions.assertEquals("admin", user.getLogin());
    }

    @Test
    public void addUserDbTest() {
        User user = new User("dbUser" + Utils.getRandomInt(), "dbPassword");
      //  userDbService.save(user);
        Assertions.assertNotNull(userDbService.getUserByLogin("dbUser"));
    }

    @Test
    public void addDublicateDbTest() {
        User user = new User("admin", "admin");
     //   Assertions.assertThrows(RollbackException.class, () -> userDbService.save(user));
    }

    @Test
    public void deleteTest() {
        userDbService.delete(161);
    }

    @Test
    public void integrationTest() {
//        enititymodels.User user = new enititymodels.User();
//        user.setLogin("db");
//        user.setPass("db");
//        user.setGames(new HashSet<Game>(){{add(new Game());}});
        User user = new User();
        user.setLogin("dbvalue3");
        user.setPass("keks2");
        user.setGames(Collections.singletonList(Utils.generateRandomGame()));
        userDbService.save(user);
     //   userService.login(user);
    }
}
