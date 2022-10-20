package models.game;

public class Requirements {
    private String osName;
    private Integer ramGb;
    private Integer hardDrive;
    private String videoCard;

    public Requirements(String osName, Integer ramGb, Integer hardDrive, String videoCard) {
        this.osName = osName;
        this.ramGb = ramGb;
        this.hardDrive = hardDrive;
        this.videoCard = videoCard;
    }

    public Requirements() {
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public Integer getRamGb() {
        return ramGb;
    }

    public void setRamGb(Integer ramGb) {
        this.ramGb = ramGb;
    }

    public Integer getHardDrive() {
        return hardDrive;
    }

    public void setHardDrive(Integer hardDrive) {
        this.hardDrive = hardDrive;
    }

    public String getVideoCard() {
        return videoCard;
    }

    public void setVideoCard(String videoCard) {
        this.videoCard = videoCard;
    }
}
