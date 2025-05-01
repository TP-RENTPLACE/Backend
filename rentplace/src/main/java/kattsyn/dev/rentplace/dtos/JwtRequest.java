package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    @Schema(description = "Почта пользователя", example = "warshard1337@gmail.com")
    private String email;
    @Schema(description = "Код, который пользователь получил на почту", example = "123456")
    private String code;

}
