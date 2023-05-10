package models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.messages.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponse {
    private User register_data;
    private Message info;
}
