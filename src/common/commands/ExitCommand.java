package common.commands;

import server.CollectionManager;

public class ExitCommand extends Command {
	private static final long serialVersionUID = 1L;
    public ExitCommand() {
        super("exit");
    }

    @Override
    public String execute(CollectionManager collection) {
        return "Команда exit должна обрабатываться клиентом";
    }
}