package common.commands;

import common.data.Route;
import common.exceptions.EmptyCollectionException;
import server.CollectionManager;

public class MinByCreationDateCommand extends Command {
	private static final long serialVersionUID = 1L;
    public MinByCreationDateCommand() {
        super("min_by_creation_date");
    }

    @Override
    public String execute(CollectionManager collection) {
        try {
            Route minRoute = collection.getMinByCreationDate();
            return "Объект с минимальной датой:\n" + minRoute;
        } catch (EmptyCollectionException e) {
            return "Коллекция пуста";
        }
    }
}