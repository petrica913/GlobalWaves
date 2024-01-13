package app.user.CommandSubscribe;

public class SubscribeInvoker {
    private SubscribeCommand subscribeCommand;

    public SubscribeInvoker(SubscribeCommand subscribeCommand) {
        this.subscribeCommand = subscribeCommand;
    }

    public void executeCommand() {
        subscribeCommand.execute();
    }
}