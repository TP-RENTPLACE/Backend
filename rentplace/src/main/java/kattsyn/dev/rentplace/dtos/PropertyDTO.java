package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO имущества")
public class PropertyDTO {

    @Schema(description = "Адрес имущества", example = "Россия, Воронеж, ул. Новосибирская, д.21")
    private String address;
    @Schema(description = "Описание имущества", example = "Уютная квартира с видом на водохранилище")
    private String description;
    @Schema(description = "Рейтинг жилья", example = "4.41")
    private float rating;
    @Schema(description = "Стоимость жилья в сутки", example = "3500")
    private int costPerDay;
    @Schema(description = "Сдаваемая площадь", example = "34.2")
    private float area;
    @Schema(description = "Количество спален", example = "4")
    private int bedrooms;
    @Schema(description = "Количество спальных мест", example = "9")
    private int sleepingPlaces;
    @Schema(description = "Количество ванных комнат", example = "2")
    private int bathrooms;
    @Schema(description = "Максимум гостей", example = "7")
    private int maxGuests;

}
