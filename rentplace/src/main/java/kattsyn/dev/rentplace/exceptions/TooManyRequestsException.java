package kattsyn.dev.rentplace.exceptions;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends AppException{
    public TooManyRequestsException(String message) {
        super(HttpStatus.TOO_MANY_REQUESTS.value(), "TOO_MANY_REQUESTS", message);
    }
}
