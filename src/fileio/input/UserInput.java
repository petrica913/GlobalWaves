package fileio.input;

public final class UserInput {
    @lombok.Getter
    private String username;
    @lombok.Getter
    private int age;
    @lombok.Getter
    private String city;

    public UserInput() {
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "UserInput{"
                + "username='" + username + '\''
                + ", age=" + age
                + ", city='" + city + '\''
                + '}';
    }
}
