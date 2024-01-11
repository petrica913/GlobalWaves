package app.user;

import lombok.Getter;

public class Notification {
    @Getter
    private String name;
    @Getter
    private String description;
    public Notification(String name, User user){
        this.name = name;
        this.description = name + " from " + user.getUsername() + ".";
    }
}
