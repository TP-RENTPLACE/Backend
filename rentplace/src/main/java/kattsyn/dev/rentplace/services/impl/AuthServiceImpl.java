package kattsyn.dev.rentplace.services.impl;

import io.micrometer.common.lang.NonNull;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import kattsyn.dev.rentplace.dtos.requests.JwtRequest;
import kattsyn.dev.rentplace.dtos.responses.CodeResponse;
import kattsyn.dev.rentplace.dtos.responses.JwtResponse;
import kattsyn.dev.rentplace.dtos.requests.RegisterRequest;
import kattsyn.dev.rentplace.dtos.users.UserDTO;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.enums.AuthType;
import kattsyn.dev.rentplace.enums.Role;
import kattsyn.dev.rentplace.enums.UserStatus;
import kattsyn.dev.rentplace.exceptions.ForbiddenException;
import kattsyn.dev.rentplace.services.AuthService;
import kattsyn.dev.rentplace.services.RefreshTokenService;
import kattsyn.dev.rentplace.services.UserService;
import kattsyn.dev.rentplace.auth.JwtProvider;
import kattsyn.dev.rentplace.services.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final VerificationCodeService verificationCodeService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public JwtResponse register(@NonNull RegisterRequest registerRequest, HttpServletRequest httpServletRequest) throws AuthException {
        User user = userService.createUserWithRegisterRequest(registerRequest);

        return getJwtResponse(user, registerRequest.getEmail(), registerRequest.getCode(), httpServletRequest);
    }

    @Override
    public CodeResponse getCodeResponse(String email) {
        Optional<User> user = userService.getUserOptionalByEmail(email);
        CodeResponse codeResponse;
        codeResponse = user.map(value -> new CodeResponse(AuthType.AUTH_LOGIN, value.getUserStatus()))
                .orElseGet(() -> new CodeResponse(AuthType.AUTH_REGISTER, null));

        if (codeResponse.getAuthType() == AuthType.AUTH_REGISTER
                || (user.isPresent()
                && codeResponse.getAuthType() == AuthType.AUTH_LOGIN
                && user.get().getUserStatus() == UserStatus.STATUS_ACTIVE)) {
            new Thread(() -> verificationCodeService.generateAndSendCode(email)).start();
        }

        return codeResponse;
    }

    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest, HttpServletRequest httpServletRequest) throws AuthException {
        final User user = userService.getUserByEmail(authRequest.getEmail());

        return getJwtResponse(user, authRequest.getEmail(), authRequest.getCode(), httpServletRequest);
    }

    @Override
    public JwtResponse adminLogin(@NonNull JwtRequest authRequest, HttpServletRequest httpServletRequest) throws AuthException {
        final User user = userService.getUserByEmail(authRequest.getEmail());

        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new ForbiddenException("You are not allowed to access admin-panel.");
        }

        return getJwtResponse(user, authRequest.getEmail(), authRequest.getCode(), httpServletRequest);
    }

    private JwtResponse getJwtResponse(User user, String email, String code, HttpServletRequest httpServletRequest) throws AuthException {
        if ((email.equals("testadmin@gmail.com") && code.equals("12345")) || verificationCodeService.validateCode(email, code)) { //todo: delete test user
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshTokenService.put(refreshToken, user, httpServletRequest);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Код неправильный");
        }
    }

    @Override
    public void validateCode(JwtRequest request) {
        verificationCodeService.validateCode(request.getEmail(), request.getCode());
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken, HttpServletRequest httpServletRequest) {
        try {
            return refreshTokenService.refreshAccessToken(refreshToken, httpServletRequest);
        } catch (AuthException e) {
            return new JwtResponse(null, null);
        }
    }

    public JwtResponse refresh(@NonNull String refreshToken, HttpServletRequest request) throws AuthException {
        return refreshTokenService.refresh(refreshToken, request);
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
}