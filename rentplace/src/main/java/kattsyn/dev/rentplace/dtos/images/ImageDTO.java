package kattsyn.dev.rentplace.dtos.images;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO Картинки")
public class ImageDTO {

    @Schema(description = "ID Фотографии")
    private long imageId;
    @Schema(description = "URL фотографии")
    private String url;
    @Schema(description = "Является ли картинка превью (для объявлений)")
    private boolean isPreviewImage;

}
