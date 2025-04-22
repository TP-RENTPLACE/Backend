package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "DTO объявления, с которым связано бронирование")
    private PropertyDTO propertyDTO;

    @Schema(description = "DTO арендатора")
    private UserDTO renterDTO;

    @Schema(description = "Дата начала проживания")
    private LocalDate startDate;

    @Schema(description = "Дата окончания проживания")
    private LocalDate endDate;

    @Schema(description = "Долгосрочная ли аренда. True - долгосрочная (по месяцам), False - посуточная")
    private boolean isLongTermRent;

    @Schema(description = "Стоимость аренды за период. Если долгосрочная аренда, то за месяц, иначе за день.")
    private int costInPeriod;

    @Schema(description = "Сумма комиссии для пользователя")
    private int renterCommission;

    @Schema(description = "Сумма комиссии для арендатора")
    private int ownerCommission;

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
