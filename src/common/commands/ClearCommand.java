package common.commands;

import server.CollectionManager;

public class ClearCommand extends Command {
	private static final long serialVersionUID = 1L;
    public ClearCommand() {
        super("clear");
    }

    @Override
    public String execute(CollectionManager collection) {
        collection.clear();
        return "Коллекция очищена";
    }
}