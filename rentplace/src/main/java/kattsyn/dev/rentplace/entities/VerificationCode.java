package kattsyn.dev.rentplace.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification_codes")
public class VerificationCode {

    @Id
    private String email;
    private String code;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
