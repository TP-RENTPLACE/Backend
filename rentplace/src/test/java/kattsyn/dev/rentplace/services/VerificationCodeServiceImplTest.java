package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.enums.AuthType;
import kattsyn.dev.rentplace.services.impl.VerificationCodeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kattsyn.dev.rentplace.dtos.CodeResponse;
import kattsyn.dev.rentplace.entities.VerificationCode;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.repositories.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceImplTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @InjectMocks
    private VerificationCodeServiceImpl verificationCodeService;

    @Test
    void validateCode_CodeExistsAndMatches_ReturnsTrue() {
        String email = "test@example.com";
        String code = "12345";
        VerificationCode verificationCode = new VerificationCode(email, code, LocalDateTime.now().plusMinutes(5), LocalDateTime.now());

        when(verificationCodeRepository.findById(email)).thenReturn(Optional.of(verificationCode));

        assertTrue(verificationCodeService.validateCode(email, code));
    }

    @Test
    void validateCode_CodeNotFound_ThrowsNotFoundException() {
        String email = "notfound@example.com";
        when(verificationCodeRepository.findById(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> verificationCodeService.validateCode(email, "12345"));
    }

    @Test
    void generateAndSendCode_NewCodeGeneratedAndSaved() {
        String email = "new@example.com";
        when(userService.existsByEmail(email)).thenReturn(false);
        when(verificationCodeRepository.findById(email)).thenReturn(Optional.empty());

        CodeResponse response = verificationCodeService.generateAndSendCode(email);

        verify(verificationCodeRepository).save(any(VerificationCode.class));
        verify(emailService).sendVerificationCode(eq(email), anyString());
        assertEquals(AuthType.AUTH_REGISTER, response.getAuthType());
    }

    @Test
    void generateCode_Returns5DigitCode() {
        String code = verificationCodeService.generateCode();

        assertTrue(code.matches("\\d{5}"));
    }
}