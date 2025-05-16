package kattsyn.dev.rentplace.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import kattsyn.dev.rentplace.enums.AuthType;
import kattsyn.dev.rentplace.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CodeResponse {

    @Schema(description = "Тип аутентификации. AUTH_LOGIN, если пользователь существует, иначе AUTH_REGISTER")
    private AuthType authType;
    @Schema(description = "Статус профиля пользователя. STATUS_BLOCKED, STATUS_ACTIVE, STATUS_INACTIVE")
    private UserStatus userStatus;

}
