package models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getSingleQuotes() {
        return singleQuotes;
    }

    public void setSingleQuotes(String singleQuotes) {
        this.singleQuotes = singleQuotes;
    }

    public MixedFields(String oneDigit, String _2, Boolean trueKey, String russianText, String colonTest, String singleQuotes, NumbersPow numbersPow) {
        this.oneDigit = oneDigit;
        this._2 = _2;
        this.trueKey = trueKey;
        this.russianText = russianText;
        this.colonTest = colonTest;
        this.singleQuotes = singleQuotes;
        this.numbersPow = numbersPow;
    }

    public NumbersPow getNumbersPow() {
        return numbersPow;
    }

    public void setNumbersPow(NumbersPow numbersPow) {
        this.numbersPow = numbersPow;
    }

    public MixedFields() {
    }

    public String getOneDigit() {
        return oneDigit;
    }

    public void setOneDigit(String oneDigit) {
        this.oneDigit = oneDigit;
    }

    public String get_2() {
        return _2;
    }

    public void set_2(String _2) {
        this._2 = _2;
    }

    public Boolean getTrueKey() {
        return trueKey;
    }

    public void setTrueKey(Boolean trueKey) {
        this.trueKey = trueKey;
    }

    public String getRussianText() {
        return russianText;
    }

    public void setRussianText(String russianText) {
        this.russianText = russianText;
    }

    public String getColonTest() {
        return colonTest;
    }

    public void setColonTest(String colonTest) {
        this.colonTest = colonTest;
    }
}
