package kattsyn.dev.rentplace.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private final String type = "Bearer ";
    private String accessToken;
    private String refreshToken;

}
