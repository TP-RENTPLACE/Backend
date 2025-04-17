package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDTO {

    @Schema(description = "ID удобства", example = "1")
    private long facilityId;

    @Schema(description = "Название удобства", example = "Душ")
    private String name;

    @Schema(description = "Лого удобства")
    private ImageDTO imageDTO;

}
