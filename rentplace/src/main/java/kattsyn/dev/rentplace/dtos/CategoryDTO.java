package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    @Schema(description = "ID категории", example = "1")
    private long categoryId;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Название категории", example = "Душ")
    private String name;

    @Schema(description = "Лого категории")
    private ImageDTO imageDTO;

}
