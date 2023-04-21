package db_models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Requirements {
    private String osName;
    private Integer ramGb;
    private Integer hardDrive;
    private String videoCard;

}
