package common.commands;

import server.CollectionManager;
import common.exceptions.FileAccessException;

public class SaveCommand extends Command {
	private static final long serialVersionUID = 1L;
    public SaveCommand() {
        super("save");
    }

    @Override
    public String execute(CollectionManager collection) {
        try {
            collection.saveToFile();
            return "Коллекция успешно сохранена в файл";
        } catch (FileAccessException e) {
            return "Ошибка сохранения: " + e.getMessage();
        }
    }
}