package dto;

import db_models.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Message;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessGameResponse {
    private Game register_data;
    private Message info;
}
