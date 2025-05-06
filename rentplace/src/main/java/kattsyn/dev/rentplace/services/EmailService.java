package kattsyn.dev.rentplace.services;

public interface EmailService {

    void sendVerificationCode(String email, String code);

}
