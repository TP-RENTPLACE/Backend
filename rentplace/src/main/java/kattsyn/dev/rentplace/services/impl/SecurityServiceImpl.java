package kattsyn.dev.rentplace.services.impl;

import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.repositories.UserRepository;
import kattsyn.dev.rentplace.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    @Override
    public String getCurrentUserEmail() throws AuthException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new AuthException("Пользователь не авторизован");
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof User) ? ((User) principal).getEmail() : principal.toString();
    }

    @Override
    public User getCurrentUser() throws AuthException {
        String email = getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found", email)));
    }

    @Override
    public Long getCurrentUserId() throws AuthException {
        return getCurrentUser().getUserId();
    }
}
