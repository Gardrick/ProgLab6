package common.commands;

import java.util.stream.Collectors;

import common.data.Route;
import server.CollectionManager;

public class ShowCommand extends Command {
	private static final long serialVersionUID = 1L;
    public ShowCommand() {
        super("show");
    }

    @Override
    public String execute(CollectionManager collection) {
        return collection.getSortedCollection().stream()
            .map(Route::toString)
            .collect(Collectors.joining("\n"));
    }
}