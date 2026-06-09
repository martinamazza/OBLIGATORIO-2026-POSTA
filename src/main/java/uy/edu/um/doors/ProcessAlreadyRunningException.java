package uy.edu.um.doors;

public class ProcessAlreadyRunningException extends RuntimeException {
    public ProcessAlreadyRunningException(String message) {
        super(message);
    }
}