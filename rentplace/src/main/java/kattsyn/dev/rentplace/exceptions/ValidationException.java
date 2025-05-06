package kattsyn.dev.rentplace.exceptions;

public class ValidationException extends AppException {

    public ValidationException(String message) {
        super(422, "VALIDATION_ERROR", message);
    }

}
