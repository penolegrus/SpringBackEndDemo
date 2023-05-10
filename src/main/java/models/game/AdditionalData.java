package models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class AdditionalData {
    private String dlcNameFromAnotherGame;
    private Boolean isFree;
}
