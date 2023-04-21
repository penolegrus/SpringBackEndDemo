package db_models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DLC {
    private Boolean isDlcFree;
    private String dlcName;
    private Integer rating;
    private String description;
    private Double price;
    private AdditionalData similarDlc;
}
