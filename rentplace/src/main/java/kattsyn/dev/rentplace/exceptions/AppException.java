package kattsyn.dev.rentplace.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final int httpStatus;
    private final String errorCode;

    public AppException(int httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

}
