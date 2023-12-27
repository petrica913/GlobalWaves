package app.user.Collections;

import lombok.Getter;

public class Merch {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private Integer price;
    public Merch(final String name, final String description, final Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
