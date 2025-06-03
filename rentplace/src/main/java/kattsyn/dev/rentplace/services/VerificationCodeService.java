package kattsyn.dev.rentplace.services;

public interface VerificationCodeService {
    boolean validateCode(String email, String code);

    void generateAndSendCode(String email);

    String generateCode();

}
