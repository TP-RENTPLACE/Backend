package kattsyn.dev.rentplace.utils;

import io.jsonwebtoken.Claims;
import kattsyn.dev.rentplace.auth.JwtAuthentication;
import kattsyn.dev.rentplace.enums.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilsTest {

    @Test
    void generate_JwtAuthenticationWithCorrectFields() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("user@test.com");
        when(claims.get("name", String.class)).thenReturn("Test User");
        when(claims.get("role", String.class)).thenReturn("ROLE_USER");

        JwtAuthentication auth = JwtUtils.generate(claims);

        assertEquals("user@test.com", auth.getEmail());
        assertEquals(Role.ROLE_USER, auth.getRole());
    }
}
