package models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import models.game.Game;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {

    private String login;
    private String pass;
    private List<Game> games;
}
