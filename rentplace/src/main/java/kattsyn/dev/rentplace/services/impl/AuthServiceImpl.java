package kattsyn.dev.rentplace.services.impl;

import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNull;
import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.auth.JwtAuthentication;
import kattsyn.dev.rentplace.dtos.requests.JwtRequest;
import kattsyn.dev.rentplace.dtos.responses.JwtResponse;
import kattsyn.dev.rentplace.dtos.requests.RegisterRequest;
import kattsyn.dev.rentplace.dtos.users.UserDTO;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.services.AuthService;
import kattsyn.dev.rentplace.services.UserService;
import kattsyn.dev.rentplace.auth.JwtProvider;
import kattsyn.dev.rentplace.services.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    //todo: сделать хранение Refresh Токенов в БД, вместе с ip, либо именами устройств.
    //TODO: также при превышении кол-ва макс устройств разлогинить везде пользователя

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final VerificationCodeService verificationCodeService;

    @Override
    public JwtResponse register(@NonNull RegisterRequest registerRequest) throws AuthException {
        User user = userService.createUserWithRegisterRequest(registerRequest);

        return getJwtResponse(user, registerRequest.getEmail(), registerRequest.getCode());
    }

    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = userService.getUserByEmail(authRequest.getEmail());

        return getJwtResponse(user, authRequest.getEmail(), authRequest.getCode());
    }

    private JwtResponse getJwtResponse(User user, String email, String code) throws AuthException {
        if ((email.equals("testadmin@gmail.com") && code.equals("12345")) ||verificationCodeService.validateCode(email, code)) { //todo: delete test user
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Код неправильный");
        }
    }

    @Override
    public boolean validateCode(JwtRequest request) {
        return verificationCodeService.validateCode(request.getEmail(), request.getCode());
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getUserByEmail(email);
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getUserByEmail(email);
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public UserDTO getUserInfo() throws AuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AuthException("Пользователь не авторизован");
        }
        String email = authentication.getName();
        return userService.getUserDTOByEmail(email);
    }



    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}