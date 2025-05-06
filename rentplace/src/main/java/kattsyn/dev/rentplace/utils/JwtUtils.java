package kattsyn.dev.rentplace.utils;

import io.jsonwebtoken.Claims;
import kattsyn.dev.rentplace.auth.JwtAuthentication;
import kattsyn.dev.rentplace.enums.Role;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(getRole(claims));
        jwtInfoToken.setName(claims.get("name", String.class));
        jwtInfoToken.setEmail(claims.getSubject());
        return jwtInfoToken;
    }

    private static Role getRole(Claims claims) {
        String roleValue = claims.get("role", String.class); // Сначала получите как строку
        return Role.valueOf(roleValue); // Затем преобразуйте строку в enum
    }

}
