package app.statistics;

import app.user.User;
public class UserStatistics implements Statistics{
    private final User user;
    public UserStatistics(User user) {
        this.user = user;
    }

    @Override
    public String generateStatistics() {
//        implement the logic for generating statics for user
        return null;
    }
}
