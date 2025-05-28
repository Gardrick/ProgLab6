package common.commands;

import server.CollectionManager;
import java.util.List;

public class PrintFieldDescendingDistanceCommand extends Command {
	private static final long serialVersionUID = 1L;
    public PrintFieldDescendingDistanceCommand() {
        super("print_field_descending_distance");
    }

    @Override
    public String execute(CollectionManager collection) {
        List<Long> distances = collection.getDescendingDistances();
        return "Дистанции в порядке убывания:\n" + String.join("\n", distances.stream()
                .map(String::valueOf)
                .toArray(String[]::new));
    }
}