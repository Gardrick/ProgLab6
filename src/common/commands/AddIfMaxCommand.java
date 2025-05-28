package common.commands;

import common.data.Route;
import common.exceptions.InvalidDataException;
import server.CollectionManager;

public class AddIfMaxCommand extends Command {
	private static final long serialVersionUID = 1L;
    private Route route;

    public AddIfMaxCommand() {
        super("add_if_max");
    }

    @Override
    public String execute(CollectionManager collection) {
        try {
            collection.addIfMax(route);
            return "Элемент добавлен";
        } catch (InvalidDataException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}