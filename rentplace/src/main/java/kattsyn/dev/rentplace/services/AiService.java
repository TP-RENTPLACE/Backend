package kattsyn.dev.rentplace.services;

import jakarta.security.auth.message.AuthException;
import kattsyn.dev.rentplace.dtos.ai.GenerateDescriptionRequest;

public interface AiService {

    String generateDescription(GenerateDescriptionRequest request) throws AuthException;

}
