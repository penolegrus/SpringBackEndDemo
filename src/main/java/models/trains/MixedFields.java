package models.trains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MixedFields {
    @JsonProperty("1")
    private String oneDigit;

    private String _2;

    @JsonProperty("true")
    private Boolean trueKey;
    @JsonProperty("что то на русском")
    private String russianText;
    @JsonProperty("bmw:users")
    private String colonTest;

    @JsonProperty("'single_quotes'")
    private String singleQuotes;

    private NumbersPow numbersPow;
}
