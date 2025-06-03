package kattsyn.dev.rentplace.services;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import kattsyn.dev.rentplace.dtos.requests.JwtRequest;
import kattsyn.dev.rentplace.dtos.responses.CodeResponse;
import kattsyn.dev.rentplace.dtos.responses.JwtResponse;
import kattsyn.dev.rentplace.dtos.requests.RegisterRequest;
import kattsyn.dev.rentplace.dtos.users.UserDTO;

public interface AuthService {

    CodeResponse getCodeResponse(String email);

    JwtResponse login(JwtRequest authRequest, HttpServletRequest httpServletRequest) throws AuthException;

    JwtResponse adminLogin(JwtRequest authRequest, HttpServletRequest httpServletRequest) throws AuthException;

    JwtResponse register(RegisterRequest registerRequest, HttpServletRequest httpServletRequest) throws AuthException;

    JwtResponse getAccessToken(String refreshToken, HttpServletRequest httpServletRequest) throws AuthException;

    JwtResponse refresh(String refreshToken, HttpServletRequest request) throws AuthException;

    UserDTO getUserInfo() throws AuthException;

    void validateCode(JwtRequest request) throws AuthException;
}
