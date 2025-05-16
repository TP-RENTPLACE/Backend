package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.services.impl.VerificationCodeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kattsyn.dev.rentplace.entities.VerificationCode;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.repositories.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceImplTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

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
    void generateCode_Returns5DigitCode() {
        String code = verificationCodeService.generateCode();

        assertTrue(code.matches("\\d{5}"));
    }
}