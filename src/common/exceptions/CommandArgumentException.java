package common.exceptions;

public class CommandArgumentException extends Exception {
	private static final long serialVersionUID = 1L;
    public CommandArgumentException(String message) {
        super(message);
    }
}