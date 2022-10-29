package models.trains;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@ToString
public class NumbersPow {
    private final Map<String, Object> nums = new LinkedHashMap<>();

    @JsonAnySetter
    void setNums(String key, Object value) {
        nums.put(key, value);
    }

    public Map<String, Object> getNums() {
        return nums;
    }
}
