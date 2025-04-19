package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(name = "CategoryCreateEditDTO", type = "object")
public class CategoryCreateEditDTO {

    @Schema(description = "Фото категории")
    private MultipartFile file;
    @Schema(description = "Название категории", example = "Пещера")
    private String name;

}
