package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import kattsyn.dev.rentplace.enums.AuthType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CodeResponse {

    @Schema(description = "Тип аутентификации. AUTH_LOGIN, если пользователь существует, иначе AUTH_REGISTER")
    private AuthType authType;

}
