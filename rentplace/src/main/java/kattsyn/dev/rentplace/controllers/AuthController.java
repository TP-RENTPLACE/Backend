package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kattsyn.dev.rentplace.dtos.CodeRequest;
import kattsyn.dev.rentplace.dtos.JwtRequest;
import kattsyn.dev.rentplace.dtos.JwtResponse;
import kattsyn.dev.rentplace.dtos.RefreshJwtRequest;
import kattsyn.dev.rentplace.services.AuthService;
import kattsyn.dev.rentplace.services.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Для аутентификации, регистрации, обновлении и запроса токенов и отправки запросов кода на почту.")
public class AuthController {

    private final AuthService authService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/code-request")
    @Operation(
            summary = "Запросить код по почте",
            description = "Запрос на получение кода авторизации по почте"
    )
    public ResponseEntity<JwtResponse> login(@RequestBody CodeRequest codeRequest) {
        verificationCodeService.generateAndSendCode(codeRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Запрос на авторизацию",
            description = "Получает email и код с почты. Возвращает JWT токены"
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest,
                                             HttpServletResponse response) throws AuthException {
        JwtResponse tokens = authService.login(authRequest);

        // Настройка cookie для refresh token
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // Для HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok()
                .body(new JwtResponse(tokens.getAccessToken(), null));
    }

    @Operation(
            summary = "Запрос на обновление AccessToken'а",
            description = "Получает RefreshToken, возвращает новый AccessToken"
    )
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Запрос на обновление RefreshToken'а",
            description = "Принимает еще не истекший RefreshToken и возвращает новый, продленный."
    )
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) throws AuthException {
        JwtResponse jwtResponse = authService.refresh(refreshToken);

        Cookie refreshCookie = new Cookie("refreshToken", jwtResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok()
                .body(new JwtResponse(jwtResponse.getAccessToken(), null));
    }

}
