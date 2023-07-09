package project;
import org.apache.commons.lang3.RandomStringUtils;

public class UserData {

    private String email;
    private String password;
    private String name;
    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public UserData() {
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static UserData randomUser() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(6);
        String name = RandomStringUtils.randomAlphanumeric(6);
        return new UserData(email, password, name);
    }
}
