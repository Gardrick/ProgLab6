package common.network;

import java.io.Serializable;

import common.commands.Command;

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
    private Command command;

    public Request(Command command) {
        this.command = command;
    }

    public Command getCommand() { return command; }
}