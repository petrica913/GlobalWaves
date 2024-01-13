package app.user.CommandSubscribe;

import app.user.User;

public class SubscribeFunction implements SubscribeCommand{
    private final User user;
    private final User target;
    public SubscribeFunction(User user, User target) {
        this.user = user;
        this.target = target;
    }

    @Override
    public void execute() {
        String message = null;
        if (target != null) {
            target.subscribe(user);
            if (target.getSubscribers().contains(user)) {
                message = user.getUsername() + " subscribed to "
                        + target.getUsername() + " successfully.";
            } else {
                message = user.getUsername() + " unsubscribed from "
                        + target.getUsername() + " successfully.";
            }
        }
        user.setSubscribeMessage(message);
    }
}
