package kattsyn.dev.rentplace.dtos.reservations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kattsyn.dev.rentplace.dtos.users.UserDTO;
import kattsyn.dev.rentplace.dtos.properties.PropertyDTO;
import kattsyn.dev.rentplace.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO бронирования")
public class ReservationDTO {

    @Schema(description = "Id бронирования", example = "1")
    private long reservationId;

    @NotNull
    @Schema(description = "DTO объявления, с которым связано бронирование")
    private PropertyDTO propertyDTO;

    @NotNull
    @Schema(description = "DTO арендатора")
    private UserDTO renterDTO;

    @NotNull
    @Schema(description = "Дата начала проживания")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "Дата окончания проживания")
    private LocalDate endDate;

    @NotNull
    @Schema(description = "Долгосрочная ли аренда. True - долгосрочная (по месяцам), False - посуточная")
    private boolean isLongTermRent;

    @NotNull
    @Schema(description = "Стоимость аренды за период. Если долгосрочная аренда, то за месяц, иначе за день.")
    private int costInPeriod;

    @NotNull
    @Schema(description = "Сумма комиссии для пользователя")
    private int renterCommission;

    @NotNull
    @Schema(description = "Сумма комиссии для арендатора")
    private int ownerCommission;

    @NotNull
    @Schema(description = "Общая стоимость бронирования, с учетом комиссии")
    private int totalCost;

    @Schema(description = """
            Статус бронирования.
            PAID - Если бронирование оплачено
            IN_PROCESS - В процессе оплаты
            NOT_PAID - Не оплачена. В случае если, оплата не прошла и пользователь решил отложить оплату.
            """)
    private PaymentStatus paymentStatus;

}
