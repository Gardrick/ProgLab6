package common.exceptions;

public class InvalidDataException extends Exception {
	private static final long serialVersionUID = 1L;
    public InvalidDataException() {
        super("Некорректные данные объекта");
    }

    public InvalidDataException(String message) {
        super(message);
    }
}