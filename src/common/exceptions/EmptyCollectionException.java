package common.exceptions;

public class EmptyCollectionException extends Exception {
	private static final long serialVersionUID = 1L;
    public EmptyCollectionException() {
        super("Коллекция пуста");
    }
}