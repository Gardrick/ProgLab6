package server;

import common.commands.Command;
import common.network.Request;
import common.network.Response;

public class CommandProcessor {
    private CollectionManager collectionManager;

    public CommandProcessor(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public Response process(Request request) {
        Command cmd = request.getCommand();
        return new Response(cmd.execute(collectionManager));
    }
}