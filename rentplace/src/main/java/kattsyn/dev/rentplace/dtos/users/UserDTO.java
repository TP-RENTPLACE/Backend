package kattsyn.dev.rentplace.dtos.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kattsyn.dev.rentplace.dtos.images.ImageDTO;
import kattsyn.dev.rentplace.enums.Gender;
import kattsyn.dev.rentplace.enums.Role;
import kattsyn.dev.rentplace.enums.UserStatus;
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
    @Min(value = 1, message = "id должен начинаться с 1")
    private long userId;
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
    @Schema(description = "Статус профиля пользователя. STATUS_BLOCKED, STATUS_ACTIVE или STATUS_INACTIVE")
    private UserStatus userStatus;
    @Schema(description = "Роль пользователя. ROLE_USER или ROLE_ADMIN")
    private Role role;
    @Schema(description = "Дата рождения пользователя", example = "2004-02-22")
    private LocalDate birthDate;
    @Schema(description = "email пользователя", example = "ivanivanov@gmail.com")
    @Email
    private String email;
    @Schema(description = "Аватарка пользователя")
    private ImageDTO imageDTO;

}
