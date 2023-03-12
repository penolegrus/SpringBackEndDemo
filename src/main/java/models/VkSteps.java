package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VkSteps {
    private Integer steps;
    private String apiKey;
}
