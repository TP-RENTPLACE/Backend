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

    @Schema(description = "Название удобства", example = "Душ")
    private String name;

}
