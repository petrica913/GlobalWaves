package app.user;

import lombok.Getter;

public class Notification {
    @Getter
    private String name;
    @Getter
    private String description;
    public Notification(final String name, final User user) {
        this.name = name;
        this.description = name + " from " + user.getUsername() + ".";
    }
}
