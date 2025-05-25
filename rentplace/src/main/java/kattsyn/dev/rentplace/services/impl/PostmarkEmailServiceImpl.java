package kattsyn.dev.rentplace.services.impl;

import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import kattsyn.dev.rentplace.exceptions.EmailException;
import kattsyn.dev.rentplace.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import com.postmarkapp.postmark.client.data.model.message.Message;

@Service
@Slf4j
public class PostmarkEmailServiceImpl implements EmailService {

    private final ApiClient postmarkClient;

    public PostmarkEmailServiceImpl() {
        String serverToken = "f68fd941-1f63-4e1e-a348-c8232ea2ded6";
        this.postmarkClient = Postmark.getApiClient(serverToken);
    }

    @Override
    public void sendVerificationCode(String email, String code) {
        log.info("Sending verification code to {}", email);
        try {
            Message message = new Message("support@rentplace.online", email, "Код подтверждения", String.format(
                    "Здравствуйте!\n\nВаш код подтверждения для регистрации: %s\n\nС уважением,\nКоманда rentplace",
                    code));
            postmarkClient.deliverMessage(message);
            log.info("Verification code sent successfully");

        } catch (PostmarkException | IOException e) {
            throw new EmailException("Postmark error: " + e.getMessage());
        }
    }
}

