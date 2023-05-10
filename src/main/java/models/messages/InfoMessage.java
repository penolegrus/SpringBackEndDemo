package models.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoMessage {

    private Message info;

    public InfoMessage(String status, String message){
        info = new Message(status,message);
    }
}
