package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.entities.VerificationCode;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.repositories.VerificationCodeRepository;
import kattsyn.dev.rentplace.services.EmailService;
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

    @Override
    public boolean validateCode(String email, String code) {
        VerificationCode verificationCode = verificationCodeRepository.findById(email)
                .orElseThrow(() -> new NotFoundException(String.format("Код верификации для почты: %s не найден", email)));

        return verificationCode.getCode().equals(code);
    }

    @Override
    public void generateAndSendCode(String email) {
        log.info("Generating verification code for email {}", email);
        String code = generateCode();

        VerificationCode verificationCode = verificationCodeRepository.findById(email)
                .orElse(new VerificationCode());
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationCodeRepository.save(verificationCode);
        log.info("Generated verification code {}", verificationCode.getCode());

        emailService.sendVerificationCode(email, verificationCode.getCode());

    }

    @Override
    public String generateCode() {
        return String.format("%05d", new Random().nextInt(99999));
    }
}
