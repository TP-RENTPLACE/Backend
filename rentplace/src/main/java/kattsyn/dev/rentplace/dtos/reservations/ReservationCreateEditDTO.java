package kattsyn.dev.rentplace.dtos.reservations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateEditDTO {

    @NotNull
    @Schema(description = "id объявления, с которым связано бронирование", example = "1")
    private long propertyId;

    @NotNull
    @Schema(description = "id арендатора", example = "1")
    private long renterId;

    @NotNull
    @Schema(description = "Дата начала проживания", example = "2025-04-22")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "Дата окончания проживания", example = "2025-05-22")
    private LocalDate endDate;

    @NotNull
    @Schema(description = "Долгосрочная ли аренда. True - долгосрочная (по месяцам), False - посуточная")
    private boolean isLongTermRent;

    @NotNull
    @Schema(description = "Стоимость аренды за период. Если долгосрочная аренда, то за месяц, иначе за день.", example = "2500")
    private int costInPeriod;

}
