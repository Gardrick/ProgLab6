package common.commands;

import common.exceptions.NotFoundException;
import server.CollectionManager;

public class RemoveByIdCommand extends Command {
	private static final long serialVersionUID = 1L;
    private int id;

    public RemoveByIdCommand() {
        super("remove_by_id");
    }

    @Override
    public String execute(CollectionManager collection) {
        try {
            collection.removeById(id);
            return "Элемент с ID " + id + " удален";
        } catch (NotFoundException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    public void setId(int id) {
        this.id = id;
    }
}