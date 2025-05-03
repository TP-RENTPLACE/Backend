package kattsyn.dev.rentplace.services;

import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.auth.JwtAuthentication;
import kattsyn.dev.rentplace.dtos.JwtRequest;
import kattsyn.dev.rentplace.dtos.JwtResponse;
import kattsyn.dev.rentplace.dtos.UserDTO;

public interface AuthService {

    JwtResponse login(JwtRequest authRequest) throws AuthException;

    JwtResponse getAccessToken(String refreshToken);

    JwtResponse refresh(String refreshToken) throws AuthException;

    JwtAuthentication getAuthInfo();

    UserDTO getUserInfo() throws AuthException;

}
