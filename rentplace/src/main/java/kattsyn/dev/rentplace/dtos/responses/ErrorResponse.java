package kattsyn.dev.rentplace.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "DTO Ошибки")
public class ErrorResponse {

    @Schema(description = "Время, когда вылетело исключение")
    private final LocalDateTime timestamp;
    @Schema(description = "HTTP код ошибки")
    private final int status;
    @Schema(description = "Сообщение ошибки")
    private final String message;
    @Schema(description = "Имя ошибки")
    private final String error;
    @Schema(description = "Путь ошибки")
    private final String path;

    public ErrorResponse(int status, String message, String error, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;
    }
}
