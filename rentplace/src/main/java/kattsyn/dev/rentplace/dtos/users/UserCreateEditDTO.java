package kattsyn.dev.rentplace.dtos.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kattsyn.dev.rentplace.enums.Gender;
import kattsyn.dev.rentplace.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Пользователя для создания или редактирования")
public class UserCreateEditDTO {

    @Schema(description = "Имя пользователя", example = "Иван")
    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 1, max = 100, message = "Имя должно быть от 1 до 100 символов.")
    private String name;
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 1, max = 100, message = "Фамилия быть от 1 до 100 символов.")
    private String surname;
    @Schema(description = "Пол пользователя. MALE или FEMALE")
    private Gender gender;
    @Schema(description = "Роль пользователя. ROLE_USER или ROLE_ADMIN")
    private Role role;
    @Schema(description = "Дата рождения пользователя", example = "2004-02-22")
    private LocalDate birthDate;
    @Schema(description = "email пользователя", example = "ivanivanov@gmail.com")
    @Email
    private String email;
    @Schema(description = "Аватарка пользователя, файлом")
    private MultipartFile file;

}