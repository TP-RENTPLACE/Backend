package kattsyn.dev.rentplace.dtos.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kattsyn.dev.rentplace.dtos.users.UserDTO;
import kattsyn.dev.rentplace.dtos.categories.CategoryDTO;
import kattsyn.dev.rentplace.dtos.facilities.FacilityDTO;
import kattsyn.dev.rentplace.dtos.images.ImageDTO;
import kattsyn.dev.rentplace.enums.PropertyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO имущества")
public class PropertyDTO {

    @Schema(description = "ID имущества")
    private long propertyId;

    @Schema(description = """
            Статус жилья.
            PUBLISHED (опубликовано) - объявление доступно всем пользователям.
            ON MODERATION (на модерации) - объявление на проверке у модерации. Видно только во вкладке "Сдать жилье" у владельца.
            REJECTED (отклонено) - объявление не прошло модерацию, его нужно отредактировать, чтобы попробовать еще раз отправить на модерацию.
            NOT PUBLISHED (не опубликовано) - объявление уже проходило модерацию и было опубликовано, но пользователь скрыл его.
            Если не будет внесено изменений, то можно спокойно публиковать. В случае внесения изменений нужно заново пройти модерацию.
            """)
    private PropertyStatus propertyStatus;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Заголовок объявления", example = "Дом на берегу моря")
    private String title;

    @NotBlank
    @Size(min = 1, max = 255)
    @Schema(description = "Адрес имущества", example = "Россия, Воронеж, ул. Новосибирская, д.21")
    private String address;

    @NotBlank
    @Size(min = 1, max = 2000)
    @Schema(description = "Описание имущества", example = "Уютная квартира с видом на водохранилище")
    private String description;

    @Schema(description = "Является ли долгосрочной арендой. True - долгосрочная аренда (месяц и более). False - по дням.")
    private boolean isLongTermRent;

    @Size(max = 10_000_000)
    @Schema(description = "Стоимость жилья. Если isLongTermRent true, то цена за месяц, иначе за сутки", example = "3500")
    private int cost;

    @Size(max = 10000)
    @Schema(description = "Сдаваемая площадь", example = "34.2")
    private float area;

    @Max(100)
    @Schema(description = "Кол-во комнат", example = "4")
    private int rooms;

    @Max(100)
    @Schema(description = "Количество спален", example = "4")
    private int bedrooms;

    @Max(200)
    @Schema(description = "Количество спальных мест", example = "9")
    private int sleepingPlaces;

    @Max(100)
    @Schema(description = "Количество ванных комнат", example = "2")
    private int bathrooms;

    @Max(200)
    @Schema(description = "Максимум гостей", example = "7")
    private int maxGuests;

    @NotNull
    @Schema(description = "Владелец жилья")
    private UserDTO ownerDTO;

    @Schema(description = "Фотографии жилья")
    private List<ImageDTO> imagesDTOs;

    @Schema(description = "Категории жилья")
    private Set<CategoryDTO> categoriesDTOs;

    @Schema(description = "Удобства жилья")
    private Set<FacilityDTO> facilitiesDTOs;

}
