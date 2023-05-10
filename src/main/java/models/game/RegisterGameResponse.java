package models.game;

import db_models.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterGameResponse {
    private Game register_data;
    private Message info;
}
