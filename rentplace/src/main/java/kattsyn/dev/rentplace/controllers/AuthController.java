package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.dtos.CodeRequest;
import kattsyn.dev.rentplace.dtos.JwtRequest;
import kattsyn.dev.rentplace.dtos.JwtResponse;
import kattsyn.dev.rentplace.dtos.RefreshJwtRequest;
import kattsyn.dev.rentplace.services.AuthService;
import kattsyn.dev.rentplace.services.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
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
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
