package models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DLC {
    private Boolean isDlcFree;
    private String dlcName;
    private Integer rating;
    private String description;
    private Double price;
    private AdditionalData similarDlc;
}
