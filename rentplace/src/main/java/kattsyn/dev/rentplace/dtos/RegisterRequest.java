package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @Schema(description = "Почта пользователя", example = "warshard1337@gmail.com")
    private String email;
    @Schema(description = "Код, который пользователь получил на почту", example = "12345")
    private String code;
    @Schema(description = "Имя нового пользователя")
    private String name;
    @Schema(description = "Фамилия нового пользователя")
    private String surname;

}
