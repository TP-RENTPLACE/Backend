package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Пользователя")
public class UserDTO {

    @Schema(description = "ID пользователя", example = "1")
    private long id;
    @Schema(description = "Имя пользователя", example = "Иван")
    private String name;
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String surname;
    @Schema(description = "Дата рождения пользователя", example = "Пока хз")
    private LocalDate birthDate;
    @Schema(description = "email пользователя", example = "ivanivanov@gmail.com")
    private String email;
    @Schema(description = "Аватарка пользователя")
    private ImageDTO imageDTO;

}
