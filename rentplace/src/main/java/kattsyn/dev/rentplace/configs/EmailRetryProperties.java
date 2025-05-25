package kattsyn.dev.rentplace.configs;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email.retry")
public record EmailRetryProperties(
        @NotBlank Integer maxAttempts,
        @NotBlank Integer delayMs
) {
}
