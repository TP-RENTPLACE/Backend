package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.configs.EmailRetryProperties;
import kattsyn.dev.rentplace.exceptions.EmailException;
import kattsyn.dev.rentplace.exceptions.EmailServiceUnavailableException;
import kattsyn.dev.rentplace.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
@EnableConfigurationProperties(EmailRetryProperties.class)
public class CompositeEmailService implements EmailService {

    private final PostmarkEmailServiceImpl postmarkEmailServiceImpl;
    private final EmailServiceImpl emailServiceImpl;
    private final EmailRetryProperties retryProperties;

    @Override
    public void sendVerificationCode(String email, String code) {
        try {
            postmarkEmailServiceImpl.sendVerificationCode(email, code);
        } catch (EmailException postmarkException) {
            log.warn("Postmark failed, trying JavaMail. Reason: {}", postmarkException.getMessage());
            try {
                emailServiceImpl.sendVerificationCode(email, code);
            } catch (EmailException javaMailEx) {
                log.error("All email services failed: {}", javaMailEx.getMessage());
                throw new EmailServiceUnavailableException("All email providers failed");
            }
        }
    }
}