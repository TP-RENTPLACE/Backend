package kattsyn.dev.rentplace.enums;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Тип загружаемого изображения",
        allowableValues = {"PROPERTY", "USER", "FACILITY", "CATEGORY"},
        example = "PROPERTY"
)
public enum ImageType {
    @Schema(description = "Изображение недвижимости")
    PROPERTY("/properties/"),
    @Schema(description = "Изображение пользователя")
    USER("/users/"),
    @Schema(description = "Изображение удобства")
    FACILITY("/facilities/"),
    @Schema(description = "Изображение категории")
    CATEGORY("/categories/"),;

    public final String additionalPath;

    ImageType(String additionalPath) {
        this.additionalPath = additionalPath;
    }
}
