package uy.edu.um.doors;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message);
    }
}