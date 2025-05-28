package common.commands;

import common.data.Route;
import common.exceptions.InvalidDataException;
import server.CollectionManager;

public class AddCommand extends Command {
	private static final long serialVersionUID = 1L;
    private Route route;

    public AddCommand() {
        super("add");
    }

    @Override
    public String execute(CollectionManager collection) {
        try {
        	Route newRoute = new Route();
        	newRoute.setName(route.getName());
            newRoute.setCoordinates(route.getCoordinates());
            newRoute.setFrom(route.getFrom());
            newRoute.setTo(route.getTo());
            newRoute.setDistance(route.getDistance());
            collection.add(newRoute);
            return "Элемент добавлен";
        } catch (InvalidDataException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}