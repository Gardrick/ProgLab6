package common.commands;

import server.CollectionManager;
import common.data.Route;
import common.exceptions.EmptyCollectionException;

public class RemoveGreaterCommand extends Command {
	private static final long serialVersionUID = 1L;
    private Route route;

    public RemoveGreaterCommand() {
        super("remove_greater");
    }

    @Override
    public String execute(CollectionManager collection) {
    	try {
            int count = collection.removeGreater(route);
            return "Удалено элементов: " + count;
        } catch (EmptyCollectionException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}