package kattsyn.dev.rentplace.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(OpenRouterProperties.class)
public class OpenRouterConfig {

    @Bean
    public WebClient webClient(OpenRouterProperties props) {
        return WebClient.builder()
                .baseUrl(props.url())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.key())
                .build();
    }

}
