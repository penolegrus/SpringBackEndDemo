package dto;

import db_models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessRegisterResponse {
    private User register_data;
    private Message info;
}
