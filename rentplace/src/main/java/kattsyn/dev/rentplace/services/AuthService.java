package kattsyn.dev.rentplace.services;

import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.auth.JwtAuthentication;
import kattsyn.dev.rentplace.dtos.requests.JwtRequest;
import kattsyn.dev.rentplace.dtos.responses.JwtResponse;
import kattsyn.dev.rentplace.dtos.requests.RegisterRequest;
import kattsyn.dev.rentplace.dtos.users.UserDTO;

public interface AuthService {

    JwtResponse login(JwtRequest authRequest) throws AuthException;

    JwtResponse register(RegisterRequest registerRequest) throws AuthException;

    JwtResponse getAccessToken(String refreshToken);

    JwtResponse refresh(String refreshToken) throws AuthException;

    JwtAuthentication getAuthInfo();

    UserDTO getUserInfo() throws AuthException;

    boolean validateCode(JwtRequest request) throws AuthException;
}
