package kattsyn.dev.rentplace.services;

import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.auth.JwtProvider;
import kattsyn.dev.rentplace.dtos.requests.JwtRequest;
import kattsyn.dev.rentplace.dtos.responses.JwtResponse;
import kattsyn.dev.rentplace.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kattsyn.dev.rentplace.entities.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    /*
    @Mock
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private VerificationCodeService verificationCodeService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_ValidCode_ReturnsTokens() throws AuthException {
        // Arrange
        JwtRequest request = new JwtRequest("user@test.com", "12345");
        User user = new User();
        when(userService.getUserByEmail(request.getEmail())).thenReturn(user);
        when(verificationCodeService.validateCode(request.getEmail(), request.getCode())).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn("access");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("refresh");

        // Act
        JwtResponse response = new JwtResponse("access", "refresh");

        // Assert
        assertEquals("access", response.getAccessToken());
        assertEquals("refresh", response.getRefreshToken());
    }

     */


}
