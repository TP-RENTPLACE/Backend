package kattsyn.dev.rentplace.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id категории", example = "3")
    @Column(name = "category_id", nullable = false)
    private long categoryId;
    @Column(name = "name", nullable = false)
    @Schema(description = "Название категории", example = "Кемпинг")
    private String name;

}
