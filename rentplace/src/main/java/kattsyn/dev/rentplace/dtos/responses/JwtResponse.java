package kattsyn.dev.rentplace.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    @Schema(example = "Bearer ")
    private final String type = "Bearer ";
    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3YXJzaGFyZDEzMzdAZ21haWwuY29tIiwiZXhwIjoxNzQ2MzA1ODEzLCJyb2xlIjoiUk9MRV9B211JTiIsIm5hbWUiOiJhZG1pbiJ9.4Rg7E39Y4baT9Eld_pkvH0D6S72eepmyd17Ch44K5Fikw32BSbXsnVq4EOnXJgXsQkmkhZrGDHZh-cSGg7pLPg")
    private String accessToken;

    //@JsonIgnore
    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3YXJzaGFyZDEzMzdAZ21haWwuY29tIiwiZXhwIjoxNzQ2MzA1ODEzLCJyb2xlIjoiUk9MRV9Bda1JTiIsIm5hbWUiOiJhZG1pbiJ9.4Rg7E39Y4baT9Eld_pkvH0D6S72eepmydCLCh44K5FikwkdBSbXsnVq4EOnXJgXsQkmkhZrGDHZh-cSGg7pLPg")
    private String refreshToken;

}
