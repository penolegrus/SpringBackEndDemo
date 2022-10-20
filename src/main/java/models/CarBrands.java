package models;

import java.util.List;

public class CarBrands {
    private String brand;

    public CarBrands(String brand, List<String> models) {
        this.brand = brand;
        this.models = models;
    }

    public CarBrands() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    private List<String> models;
}
