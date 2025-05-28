package common.exceptions;

public class InvalidArgumentException extends Exception {
	private static final long serialVersionUID = 1L;
    public InvalidArgumentException() {
        super("Некорректные аргументы команды");
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}