package kattsyn.dev.rentplace.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.configs.OpenRouterProperties;
import kattsyn.dev.rentplace.dtos.ai.GenerateDescriptionRequest;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.exceptions.TooManyRequestsException;
import kattsyn.dev.rentplace.services.AiService;
import kattsyn.dev.rentplace.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final OpenRouterProperties openRouterProperties;
    private final WebClient openRouterWebClient;
    private final SecurityService securityService;

    private final ConcurrentMap<Long, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public String generateDescription(GenerateDescriptionRequest request) throws AuthException {
        User user = securityService.getCurrentUser();

        Bucket bucket = buckets.computeIfAbsent(user.getUserId(), this::newBucket);
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestsException("Превышен лимит AI-запросов (макс 10 в час)");
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", openRouterProperties.defaultSystemPrompt()
        ));
        if (request.getSystemPrompt() != null && !request.getSystemPrompt().isBlank()) {
            messages.add(Map.of(
                    "role", "system",
                    "content", request.getSystemPrompt()
            ));
        }
        messages.add(Map.of(
                "role",    "user",
                "content", request.getUserPrompt()
        ));

        JsonNode response = openRouterWebClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", openRouterProperties.model(),
                        "messages", messages,
                        "stream", false,
                        "response_format", Map.of("type", "text")
                ))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String raw = response
                .path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText("");

        String cleaned = raw.replaceAll("(?s)<think>.*?</think>\\s*", "").trim();

        return cleaned;
    }

    private Bucket newBucket(Long userId) {
        Refill refill   = Refill.intervally(10, Duration.ofHours(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}
