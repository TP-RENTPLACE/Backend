package kattsyn.dev.rentplace.configs;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openrouter.api")
public record OpenRouterProperties(
        @NotBlank String url,
        @NotBlank String key,
        @NotBlank String defaultSystemPrompt,
        @NotBlank String model) {
}
