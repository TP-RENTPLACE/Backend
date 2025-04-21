package kattsyn.dev.rentplace.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import kattsyn.dev.rentplace.enums.PropertyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO имущества")
public class PropertyCreateEditDTO {

    @Schema(description = """
            Статус жилья.
            PUBLISHED (опубликовано) - объявление доступно всем пользователям.
            ON MODERATION (на модерации) - объявление на проверке у модерации. Видно только во вкладке "Сдать жилье" у владельца.
            REJECTED (отклонено) - объявление не прошло модерацию, его нужно отредактировать, чтобы попробовать еще раз отправить на модерацию.
            NOT PUBLISHED (не опубликовано) - объявление уже проходило модерацию и было опубликовано, но пользователь скрыл его.
            Если не будет внесено изменений, то можно спокойно публиковать. В случае внесения изменений нужно заново пройти модерацию.
            """)
    private PropertyStatus propertyStatus;

    @Schema(description = "Заголовок объявления", example = "Дом на берегу моря")
    private String title;

    @Schema(description = "Адрес имущества", example = "Россия, Воронеж, ул. Новосибирская, д.21")
    private String address;

    @Schema(description = "Описание имущества", example = "Уютная квартира с видом на водохранилище")
    private String description;

    @Schema(description = "Является ли долгосрочной арендой. True - долгосрочная аренда (месяц и более). False - по дням.")
    private boolean isLongTermRent;

    @Schema(description = "Стоимость жилья. Если isLongTermRent true, то цена за месяц, иначе за сутки", example = "3500")
    private int cost;

    @Schema(description = "Сдаваемая площадь", example = "34.2")
    private float area;

    @Schema(description = "Кол-во комнат", example = "4")
    private int rooms;

    @Schema(description = "Количество спален", example = "4")
    private int bedrooms;

    @Schema(description = "Количество спальных мест", example = "9")
    private int sleepingPlaces;

    @Schema(description = "Количество ванных комнат", example = "2")
    private int bathrooms;

    @Schema(description = "Максимум гостей", example = "7")
    private int maxGuests;

    @Schema(description = "Владелец жилья", example = "1")
    private Long ownerId;

    @Schema(description = "Фотографии объявления, файлами")
    private MultipartFile[] files;

    @Schema(description = "Id категорий объявления")
    private Long[] categoriesIds;

    @Schema(description = "Id удобств объявления")
    private Long[] facilitiesIds;

}
