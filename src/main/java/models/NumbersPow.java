package models;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

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
