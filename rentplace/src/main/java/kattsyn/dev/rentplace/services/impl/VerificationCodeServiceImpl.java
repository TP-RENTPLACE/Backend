package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.dtos.CodeResponse;
import kattsyn.dev.rentplace.entities.VerificationCode;
import kattsyn.dev.rentplace.enums.AuthType;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.repositories.VerificationCodeRepository;
import kattsyn.dev.rentplace.services.EmailService;
import kattsyn.dev.rentplace.services.UserService;
import kattsyn.dev.rentplace.services.VerificationCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Override
    public boolean validateCode(String email, String code) {
        VerificationCode verificationCode = verificationCodeRepository.findById(email)
                .orElseThrow(() -> new NotFoundException(String.format("Verification code with email %s not found", email)));

        return verificationCode.getCode().equals(code);
    }

    @Override
    public CodeResponse generateAndSendCode(String email) {
        log.info("Generating verification code for email {}", email);
        String code = generateCode();

        VerificationCode verificationCode = verificationCodeRepository.findById(email)
                .orElse(new VerificationCode());
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationCodeRepository.save(verificationCode);
        log.info("Generated verification code {}", verificationCode.getCode());

        CodeResponse codeResponse = new CodeResponse(userService.existsByEmail(email) ? AuthType.AUTH_LOGIN : AuthType.AUTH_REGISTER);

        emailService.sendVerificationCode(email, code);

        return codeResponse;
    }

    @Override
    public String generateCode() {
        return String.format("%05d", new Random().nextInt(99999));
    }
}
