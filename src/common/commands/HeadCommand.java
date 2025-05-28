package common.commands;

import java.util.Optional;

import common.data.Route;
import server.CollectionManager;

public class HeadCommand extends Command {
	private static final long serialVersionUID = 1L;
    public HeadCommand() {
        super("head");
    }

    @Override
    public String execute(CollectionManager collection) {
        Optional<Route> head = collection.getHead();
        return head.isPresent() 
            ? head.get().toString() 
            : "Коллекция пуста";
    }
}