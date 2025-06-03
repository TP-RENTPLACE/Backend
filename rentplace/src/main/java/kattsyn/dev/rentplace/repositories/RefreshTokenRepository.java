package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("""
                SELECT DISTINCT r
                FROM RefreshToken r
                WHERE r.user.userId=:userId
            """)
    List<RefreshToken> findAllByUserId(Long userId);

    @Query("""
                SELECT DISTINCT r
                FROM RefreshToken r
                WHERE r.user.userId=:userId AND r.token=:refreshToken
            """)
    Optional<RefreshToken> findByUserAndToken(Long userId, String refreshToken);

    void deleteByCreatedAtBefore(LocalDateTime createdAt);
}
