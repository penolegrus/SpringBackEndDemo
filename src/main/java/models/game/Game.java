package models.game;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue
    @Column(name = "game_id")
    private Long gameId;
    @Column(name = "title")
    private String title;
    @Column(name = "genre")

    private String genre;
    @Column(name = "required_age")
    private Boolean requiredAge;
    @Column(name = "is_free")
    private Boolean isFree;
    @Column(name = "price")
    private Double price;
    @Column(name = "company")
    private String company;
    @Column(name = "publish_date")
    private LocalDateTime publish_date;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "description")
    private String description;
    @ElementCollection
    @Column(name="game_tags")
    private List<String> tags;
    @ElementCollection(targetClass=DLC.class)
    @Column(name = "game_dlcs")
    private List<DLC> dlcs;
    private Requirements requirements;

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
    public boolean isTypeOfNewFieldCorrect(UpdField updField){
        try {
            return this.getClass().getDeclaredField(updField.getFieldName()).getType().isAssignableFrom(updField.getValue().getClass());
        } catch (Exception ignored){}
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Game game = (Game) o;
        return getGameId() != null && Objects.equals(getGameId(), game.getGameId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
