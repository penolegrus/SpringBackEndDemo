package models;

public class ChangeUserPass {
    private String password;

    public ChangeUserPass(String password) {
        this.password = password;
    }

    public ChangeUserPass() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
