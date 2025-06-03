package kattsyn.dev.rentplace.schedulers;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredRefreshTokenCleanupTask {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.expiration-time-in-days.refresh}")
    private int refreshTokenExpTimeInDays;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanExpiredRefreshTokens() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(refreshTokenExpTimeInDays);
        refreshTokenRepository.deleteByCreatedAtBefore(cutoff);
    }

}
