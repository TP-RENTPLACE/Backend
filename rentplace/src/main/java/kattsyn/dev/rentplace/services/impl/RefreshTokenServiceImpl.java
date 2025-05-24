package kattsyn.dev.rentplace.services.impl;

import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNull;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import kattsyn.dev.rentplace.auth.JwtProvider;
import kattsyn.dev.rentplace.dtos.responses.JwtResponse;
import kattsyn.dev.rentplace.entities.RefreshToken;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.repositories.RefreshTokenRepository;
import kattsyn.dev.rentplace.services.RefreshTokenService;
import kattsyn.dev.rentplace.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.max-devices}")
    private int maxDevices;

    @Override
    public void put(String refreshToken, User user, HttpServletRequest httpServletRequest) {
        deleteOldestRefreshToken(user);

        RefreshToken newToken = new RefreshToken();
        newToken.setUser(user);
        newToken.setToken(refreshToken);
        newToken.setDeviceHash(getDeviceHash(httpServletRequest));
        newToken.setCreatedAt(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        refreshTokenRepository.save(newToken);
    }

    @Override
    public JwtResponse refreshAccessToken(@NonNull String refreshToken, HttpServletRequest request) throws AuthException {
        RefreshData refreshData = processRefreshToken(refreshToken, request);

        String newAccessToken = jwtProvider.generateAccessToken(refreshData.user());

        return new JwtResponse(newAccessToken, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken, HttpServletRequest request) throws AuthException {
        RefreshData refreshData = processRefreshToken(refreshToken, request);

        deleteOldestRefreshToken(refreshData.user());

        String newAccessToken = jwtProvider.generateAccessToken(refreshData.user());
        String newRefreshToken = jwtProvider.generateRefreshToken(refreshData.user());

        updateStoredToken(refreshData.storedToken(), newRefreshToken);

        return new JwtResponse(newAccessToken, newRefreshToken);
    }

    private RefreshData processRefreshToken(String refreshToken, HttpServletRequest request) throws AuthException {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException("Невалидный токен");
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String email = claims.getSubject();

        String currentDeviceHash = getDeviceHash(request);

        User user = userService.getUserByEmail(email);
        RefreshToken storedToken = refreshTokenRepository.findByUserAndToken(user.getUserId(), refreshToken)
                .orElseThrow(() -> new AuthException("Токен не найден"));

        if (!storedToken.getDeviceHash().equals(currentDeviceHash)) {
            throw new AuthException("Несоответствие устройства");
        }

        return new RefreshData(user, storedToken);
    }

    private void updateStoredToken(RefreshToken storedToken, String newRefreshToken) {
        storedToken.setToken(newRefreshToken);
        storedToken.setCreatedAt(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        refreshTokenRepository.save(storedToken);
    }

    private record RefreshData(User user, RefreshToken storedToken) {}

    private String getDeviceHash(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return DigestUtils.sha256Hex(ip + userAgent);
    }

    private void deleteOldestRefreshToken(User user) {
        List<RefreshToken> userTokens = refreshTokenRepository.findAllByUserId(user.getUserId());
        if (userTokens.size() >= maxDevices) {
            RefreshToken oldestToken = userTokens.stream()
                    .min(Comparator.comparing(RefreshToken::getCreatedAt)).orElseThrow();
            refreshTokenRepository.delete(oldestToken);
        }
    }
}
