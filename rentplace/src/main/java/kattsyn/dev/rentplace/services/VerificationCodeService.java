package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.responses.CodeResponse;

public interface VerificationCodeService {
    boolean validateCode(String email, String code);

    CodeResponse generateAndSendCode(String email);

    String generateCode();

}
