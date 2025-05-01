package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.path}/images")
@Slf4j
@Tag(name = "ImageController", description = "Для взаимодействия с фотографиями")
public class ImageController {

    private final ImageService imageService;
    private final Path rootLocation;

    public ImageController(ImageService imageService, @Value("${upload.path}") String uploadPath) {
        this.imageService = imageService;
        this.rootLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
    }

    @GetMapping("/{entityType}/{entityId}/{filename:.+}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @PathVariable String filename) {

        Path filePath = rootLocation.resolve(entityType)
                .resolve(entityId.toString())
                .resolve(filename)
                .normalize();

        log.info(filePath.toString());

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Загрузка фотографии",
            description = "Загрузка фотографии"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> uploadImage(
            @Parameter(
                    description = "Файл фотографии",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) @RequestParam("file") MultipartFile file,
            @Parameter(
                    description = "Тип изображения",
                    required = true,
                    schema = @Schema(
                            implementation = ImageType.class)
            ) @RequestParam ImageType imageType) { //todo: delete method
        return ResponseEntity.ok(imageService.uploadImage(file, imageType));
    }



    @PostMapping(value = "/multiple/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Массовая загрузка фотографий",
            description = "Загружает несколько фотографий за один запрос. Максимальное количество файлов - 10. Максимальный размер каждого файла - 10MB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фотографии успешно загружены", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image[].class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос (превышено кол-во файлов, неверный формат и т.д.)", content = @Content),
            @ApiResponse(responseCode = "413", description = "Превышен максимальный размер запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Image>> uploadMultipleImages(@Parameter(
            description = "Массив файлов фотографий",
            required = true,
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary"))
    ) @RequestPart("files") MultipartFile[] files) {
        List<Image> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            savedImages.add(imageService.uploadImage(file));
        }

        return ResponseEntity.ok(savedImages);
    }

    @Operation(
            summary = "Получить фотографию",
            description = "Получить фотографию по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Фотография не найдена", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImage(
            @Parameter(description = "ID фотографии", required = true, example = "1")
            @PathVariable long id) {
        Image image = imageService.getImageById(id);
        Resource file = imageService.getImageResource(image);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getOriginalFileName() + "\"")
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(file);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить фотографию",
            description = "Удаляет фотографию и её метаданные",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фото успешно удалено"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Фото не найдено")
            }
    )
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteImage(
            @Parameter(description = "ID фотографии", required = true, example = "1")
            @PathVariable Long id) {

        imageService.deleteImage(id);

        return ResponseEntity.ok().build();
    }
}
