package fileio.input;

import java.util.ArrayList;

public final class Input {
    @lombok.Getter
    private LibraryInput library;
    @lombok.Getter
    private ArrayList<UserInput> users;
    @lombok.Getter
    private ArrayList<CommandInput> commands;

    public Input() {
    }

    public void setLibrary(final LibraryInput library) {
        this.library = library;
    }

    public void setUsers(final ArrayList<UserInput> users) {
        this.users = users;
    }

    public void setCommands(final ArrayList<CommandInput> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "AppInput{"
                + "library=" + library
                + ", users=" + users
                + ", commands=" + commands
                + '}';
    }
}
