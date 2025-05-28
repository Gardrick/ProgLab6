package common.commands;

import java.io.Serializable;

import common.exceptions.InvalidArgumentException;
import server.CollectionManager;

public abstract class Command implements Serializable {
	private static final long serialVersionUID = 1L;
    private String name;
    private String args;

    public Command(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public String getArgs() { return args; }
    public void setArgs(String args) { this.args = args; }

    public abstract String execute(CollectionManager collection);
}