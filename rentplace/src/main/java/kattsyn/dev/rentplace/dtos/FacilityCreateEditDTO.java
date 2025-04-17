package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Название удобства", example = "Душ")
    private String name;

}
