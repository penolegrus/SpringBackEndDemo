package models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private Integer gameId;
    private String title;
    private String genre;
    private Boolean requiredAge;
    private Boolean isFree;
    private Double price;
    private String company;
    private LocalDateTime publish_date;
    private Integer rating;
    private String description;
    private List<String> tags;
    private List<DLC> dlcs;
    private Requirements requirements;

    public boolean isTypeOfNewFieldCorrect(UpdField updField){
        try {
            return this.getClass().getDeclaredField(updField.getFieldName()).getType().isAssignableFrom(updField.getValue().getClass());
        } catch (Exception ignored){}
        return false;
    }

    public boolean isFieldExist(String name){
        try {
            this.getClass().getDeclaredField(name);
            return true;
        } catch (Exception ignored){}
        return false;
    }

    public boolean isNewFieldHasSameValue(String name, Object newValue){
        try{
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            Object fieldValue = field.get(this);
            if(fieldValue.equals(newValue)){
                return true;
            }
        } catch (Exception ignored){}
        return false;
    }

    public Game editFieldValue(String name, Object newValue){
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(this,newValue);
            return this;
        } catch (Exception ignored){}
        return null;
    }
}
