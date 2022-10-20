package models;

public class StatusCode {
    private Integer statusCode;
    private String description;

    public StatusCode(Integer statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public StatusCode() {
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
