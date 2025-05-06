package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String email, String code) {
        log.info("Sending verification code to " + email);
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Код подтверждения");
        String text = String.format(
                "Здравствуйте!\n\nВаш код подтверждения для регистрации: %s\n\nС уважением,\nКоманда rentplace",
                code
        );
        message.setText(text);
        mailSender.send(message);
        log.info("Verification code should be sent");
    }
}
