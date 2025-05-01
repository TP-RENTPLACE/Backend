package kattsyn.dev.rentplace.configs;

import jakarta.annotation.PostConstruct;
import kattsyn.dev.rentplace.auth.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${api.path}")
    private String apiPath;
    private final JwtFilter jwtFilter;

    private String[] PUBLIC_URLS;
    private String[] PUBLIC_URLS_GET;
    private String[] ADMIN_URLS;

    @PostConstruct
    public void init() {
        PUBLIC_URLS = new String[]{
                "/" + apiPath + "/auth/register",
                "/" + apiPath + "/auth/code-request",
                "/" + apiPath + "/auth/login",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        };

        PUBLIC_URLS_GET = new String[]{
                "/" + apiPath + "/reservations/**",
                "/" + apiPath + "/properties/**",
                "/" + apiPath + "/categories/**",
                "/" + apiPath + "/facilities/**",
                "/" + apiPath + "/users/**",
                "/" + apiPath + "/images/**",
        };

        ADMIN_URLS = new String[]{
                "/" + apiPath + "/reservations/{id}",
                "/" + apiPath + "/properties/{id}",
                "/" + apiPath + "/categories/**",
                "/" + apiPath + "/facilities/**",
                "/" + apiPath + "/users/{id}",
                "/" + apiPath + "/images/{id}"
        };

        //TODO: сделать публичные URLS, публичные с GET запросом, а остальные authorized(), а для админских поставить в контроллере
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(PUBLIC_URLS).permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_URLS_GET).permitAll()
                                .requestMatchers(HttpMethod.DELETE, ADMIN_URLS).hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, ADMIN_URLS).hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PATCH, ADMIN_URLS).hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, ADMIN_URLS).hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
