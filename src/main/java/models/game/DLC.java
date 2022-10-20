package models.game;

public class DLC {
    private Boolean isDlcFree;
    private String dlcName;
    private Integer rating;
    private String description;
    private Double price;
    private AdditionalData similarDlc;

    public AdditionalData getSimilarDlc() {
        return similarDlc;
    }

    public void setSimilarDlc(AdditionalData similarDlc) {
        this.similarDlc = similarDlc;
    }

    public DLC(Boolean isDlcFree, String dlcName, Integer rating, String description, Double price, AdditionalData similarDlc) {
        this.isDlcFree = isDlcFree;
        this.dlcName = dlcName;
        this.rating = rating;
        this.description = description;
        this.price=price;
        this.similarDlc = similarDlc;
    }

    public DLC() {
    }

    public Boolean getDlcFree() {
        return isDlcFree;
    }

    public void setDlcFree(Boolean dlcFree) {
        isDlcFree = dlcFree;
    }

    public String getDlcName() {
        return dlcName;
    }

    public void setDlcName(String dlcName) {
        this.dlcName = dlcName;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
