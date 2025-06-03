package kattsyn.dev.rentplace.services;

import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.entities.User;

public interface SecurityService {

    String getCurrentUserEmail() throws AuthException;
    User getCurrentUser() throws AuthException;
    Long getCurrentUserId() throws AuthException;

}
