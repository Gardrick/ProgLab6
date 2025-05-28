package common.exceptions;

public class FileAccessException extends Exception {
	private static final long serialVersionUID = 1L;
    public FileAccessException(String message) {
        super(message);
    }
}