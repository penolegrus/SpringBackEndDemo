package models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.messages.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterGameResponse {
    private Game register_data;
    private Message info;
}
