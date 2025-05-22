package kattsyn.dev.rentplace.dtos.reservations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class PropertyReservationDTO {

    @Schema(description = "id объявления")
    private Long propertyId;

    @NotNull
    @Schema(description = "Дата начала проживания")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "Дата окончания проживания")
    private LocalDate endDate;

    @Schema(description = """
            Статус бронирования.
            PAID - Если бронирование оплачено
            IN_PROCESS - В процессе оплаты
            NOT_PAID - Не оплачена. В случае если, оплата не прошла и пользователь решил отложить оплату.
            """)
    private PaymentStatus paymentStatus;

}
