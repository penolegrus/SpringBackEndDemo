package models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Requirements {
    private String osName;
    private Integer ramGb;
    private Integer hardDrive;
    private String videoCard;


}
