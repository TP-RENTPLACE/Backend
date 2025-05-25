package kattsyn.dev.rentplace.exceptions;

public class EmailServiceUnavailableException extends RuntimeException {
    public EmailServiceUnavailableException(String message) {
        super(message);
    }
}
