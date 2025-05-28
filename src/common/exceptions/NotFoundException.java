package common.exceptions;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
    public NotFoundException() {
        super("Некорректные данные объекта");
    }

    public NotFoundException(String message) {
        super(message);
    }
}