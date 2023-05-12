package models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Requirements {
    @Column(name = "os_name")
    private String osName;
    @Column(name = "ram_gb")
    private Integer ramGb;
    @Column(name = "hard_drive")
    private Integer hardDrive;
    @Column(name = "video_card")
    private String videoCard;

}
