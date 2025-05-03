package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.dtos.*;
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
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest/*,
                                             HttpServletResponse response*/) throws AuthException {
        JwtResponse tokens = authService.login(authRequest);

        /*
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(false) // на прод вернуть true
                .sameSite("Lax") // вернуть None
                .path("/")
                .maxAge(Duration.ofDays(30)) // чтобы не исчезала
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

         */

        /*
        // Настройка cookie для refresh token
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // Для HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней

        response.addCookie(refreshTokenCookie);

         */

        return ResponseEntity.ok()
                .body(tokens);
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
    public ResponseEntity<JwtResponse> refresh(/*@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response*/ @RequestBody RefreshJwtRequest request) throws AuthException {
        JwtResponse jwtResponse = authService.refresh(request.getRefreshToken());

        /*
        Cookie refreshCookie = new Cookie("refreshToken", jwtResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(refreshCookie);



        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", jwtResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // на прод вернуть true
                .sameSite("Lax") // вернуть None
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
         */

        return ResponseEntity.ok()
                .body(jwtResponse);
    }


    @GetMapping("/info")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение информации о пользователе", description = "Возвращает информацию об авторизованном пользователе")
    public UserDTO getUserInfo() throws AuthException {
        return authService.getUserInfo();
    }

}
