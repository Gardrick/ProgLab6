package common.commands;

import server.CollectionManager;
import java.util.Map;

public class GroupCountingByIdCommand extends Command {
	private static final long serialVersionUID = 1L;
    public GroupCountingByIdCommand() {
        super("group_counting_by_id");
    }

    @Override
    public String execute(CollectionManager collection) {
        Map<Integer, Long> groups = collection.groupCountingById();
        StringBuilder sb = new StringBuilder();
        groups.forEach((id, count) -> sb.append("ID: ").append(id).append(" - ").append(count).append("\n"));
        return sb.toString().trim();
    }
}