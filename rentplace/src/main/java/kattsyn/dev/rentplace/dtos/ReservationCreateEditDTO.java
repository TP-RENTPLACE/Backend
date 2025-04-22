package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "id объявления, с которым связано бронирование", example = "1")
    private long propertyId;

    @Schema(description = "id арендатора", example = "1")
    private long renterId;

    @Schema(description = "Дата начала проживания", example = "2025-04-22")
    private LocalDate startDate;

    @Schema(description = "Дата окончания проживания", example = "2025-05-22")
    private LocalDate endDate;

    @Schema(description = "Долгосрочная ли аренда. True - долгосрочная (по месяцам), False - посуточная")
    private boolean isLongTermRent;

    @Schema(description = "Стоимость аренды за период. Если долгосрочная аренда, то за месяц, иначе за день.", example = "2500")
    private int costInPeriod;

}
