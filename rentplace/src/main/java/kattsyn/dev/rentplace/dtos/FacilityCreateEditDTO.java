package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(name = "FacilityCreateEditDTO", type = "object")
public class FacilityCreateEditDTO {

    @Schema(description = "Фото удобства")
    private MultipartFile file;
    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Название удобства", example = "Душ")
    private String name;

}
