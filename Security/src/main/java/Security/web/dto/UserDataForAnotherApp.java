package Security.web.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDataForAnotherApp {

    private String email;
    private String fullName;
    private String googleId;
    private List<String> authorities = new ArrayList<>();

    public UserDataForAnotherApp(String email, String fullName, String googleId) {
        this.email = email;
        this.fullName = fullName;
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGoogleId() {
        return googleId;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
