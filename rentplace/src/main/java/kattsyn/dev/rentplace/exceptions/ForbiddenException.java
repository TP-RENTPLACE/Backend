package kattsyn.dev.rentplace.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AppException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN.value(), "FORBIDDEN", message);
    }

}
