package kattsyn.dev.rentplace.services;

import io.micrometer.common.lang.NonNull;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import kattsyn.dev.rentplace.dtos.responses.JwtResponse;
import kattsyn.dev.rentplace.entities.User;

public interface RefreshTokenService {

    void put(String refreshToken, User user, HttpServletRequest httpServletRequest);

    JwtResponse refresh(String refreshToken, HttpServletRequest httpServletRequest) throws AuthException;

    JwtResponse refreshAccessToken(@NonNull String refreshToken, HttpServletRequest request) throws AuthException;
}
