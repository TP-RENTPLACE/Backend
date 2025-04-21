package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    @Schema(description = "ID категории", example = "1")
    private long categoryId;

    @Schema(description = "Название категории", example = "Душ")
    private String name;

    @Schema(description = "Лого категории")
    private ImageDTO imageDTO;

}
