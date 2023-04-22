package dto;


import db_models.game.Game;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String login;
    private String pass;
    private List<Game> games;

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                ", games=" + games +
                '}';
    }
}
