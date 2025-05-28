package common.commands;

import server.CollectionManager;

public class InfoCommand extends Command {
	private static final long serialVersionUID = 1L;
    public InfoCommand() {
        super("info");
    }

    @Override
    public String execute(CollectionManager collection) {
        return collection.getInfo();
    }
}