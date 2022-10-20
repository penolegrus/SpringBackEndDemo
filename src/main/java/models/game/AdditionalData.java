package models.game;

public class AdditionalData {
    private String dlcNameFromAnotherGame;
    private boolean isFree;

    public AdditionalData(String dlcNameFromAnotherGame, Boolean isFree) {
        this.dlcNameFromAnotherGame = dlcNameFromAnotherGame;
        this.isFree = isFree;
    }

    public AdditionalData() {
    }

    public String getDlcNameFromAnotherGame() {
        return dlcNameFromAnotherGame;
    }

    public void setDlcNameFromAnotherGame(String dlcNameFromAnotherGame) {
        this.dlcNameFromAnotherGame = dlcNameFromAnotherGame;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }
}
