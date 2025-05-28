package common.commands;

import common.data.Route;
import common.exceptions.InvalidDataException;
import common.exceptions.NotFoundException;
import server.CollectionManager;

public class UpdateCommand extends Command {
	private static final long serialVersionUID = 1L;
    private int id;
    private Route updatedRoute;

    public UpdateCommand() {
        super("update");
    }

    @Override
    public String execute(CollectionManager collection) {
        try {
        	Route oldRoute = collection.getById(id);
        	if (updatedRoute.getId() < 0) {
                throw new InvalidDataException("Некорректный ID");
            }
        	collection.update(id, updatedRoute);
            return "Элемент с ID " + id + " обновлен";
        } catch (NotFoundException | InvalidDataException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUpdatedRoute(Route route) {
        this.updatedRoute = route;
    }
}