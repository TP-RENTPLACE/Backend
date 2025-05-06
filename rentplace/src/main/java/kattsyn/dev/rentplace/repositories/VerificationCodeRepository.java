package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
}
