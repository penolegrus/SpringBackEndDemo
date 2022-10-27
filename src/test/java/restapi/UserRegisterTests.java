package restapi;

import helpers.Utils;
import models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;


import static helpers.Constants.REGISTER_API_URL;

public class UserRegisterTests extends BaseApiTest {


    @BeforeEach
    public void setBasePath() {
        reqBuilder.setBasePath("/api/register");
    }

    @Test
    public void registerNewUserWithAlreadyLoginExistTest() {
        User user = new User("admin", "admin");
        restService.post( user)
                .hasStatusCode(400)
                .hasMessage("Login already exist");
    }


    @Test
    public void registerNewUserWithoutPass() {
        User user = new User();
        user.setLogin("fake");
        restService.post(user)
                .hasStatusCode(400)
                .hasMessage("Missing login or password");

    }

    @Test
    public void validRegisterUser() {
        User user = new User("FAKEUSERTODAY" + Utils.getRandomInt(), "123456");

        User data = restService.post(user)
                .hasStatusCode(201)
                .hasMessage("User created")
                .as("register_data");

        Assert.assertEquals(user.getLogin(), data.getLogin());
        Assert.assertEquals(user.getPass(), data.getPass());
        Assert.assertNotNull(data.getId());
        Assert.assertNotNull(data.getGames());
    }
}
