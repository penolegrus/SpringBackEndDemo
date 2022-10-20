package models.game;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public Game() {
    }


    public Game(Integer gameId, String title, String genre, Boolean requiredAge, Boolean isFree, Double price, String company, LocalDateTime publish_date, Integer rating, String description, List<String> tags, List<DLC> dlcs, Requirements requirements) {
        this.gameId = gameId;
        this.title = title;
        this.genre = genre;
        this.requiredAge = requiredAge;
        this.isFree = isFree;
        this.price = price;
        this.company = company;
        this.publish_date = publish_date;
        this.rating = rating;
        this.description = description;
        this.tags = tags;
        this.dlcs = dlcs;
        this.requirements = requirements;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Boolean getRequiredAge() {
        return requiredAge;
    }

    public void setRequiredAge(Boolean requiredAge) {
        this.requiredAge = requiredAge;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDateTime getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(LocalDateTime publish_date) {
        this.publish_date = publish_date;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<DLC> getDlcs() {
        return dlcs;
    }

    public void setDlcs(List<DLC> dlcs) {
        this.dlcs = dlcs;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameId, game.gameId) && Objects.equals(title, game.title) && Objects.equals(genre, game.genre) && Objects.equals(requiredAge, game.requiredAge) && Objects.equals(isFree, game.isFree) && Objects.equals(price, game.price) && Objects.equals(company, game.company) && Objects.equals(publish_date, game.publish_date) && Objects.equals(rating, game.rating) && Objects.equals(description, game.description) && Objects.equals(tags, game.tags) && Objects.equals(dlcs, game.dlcs) && Objects.equals(requirements, game.requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, title, genre, requiredAge, isFree, price, company, publish_date, rating, description, tags, dlcs, requirements);
    }

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
