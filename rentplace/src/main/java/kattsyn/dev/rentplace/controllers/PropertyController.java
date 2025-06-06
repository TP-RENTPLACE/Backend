package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kattsyn.dev.rentplace.dtos.images.ImageDTO;
import kattsyn.dev.rentplace.dtos.properties.PropertyCreateEditDTO;
import kattsyn.dev.rentplace.dtos.properties.PropertyDTO;
import kattsyn.dev.rentplace.dtos.filters.PropertyFilterDTO;
import kattsyn.dev.rentplace.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("${api.path}/properties")
@Tag(name = "Property Controller", description = "Взаимодействие с имуществом")
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Массовая загрузка фотографий",
            description = "Загружает несколько фотографий за один запрос. Максимальное количество файлов - 20. Максимальный размер каждого файла - 5MB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фотографии успешно загружены", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO[].class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос (превышено кол-во файлов, неверный формат и т.д.)", content = @Content),
            @ApiResponse(responseCode = "413", description = "Превышен максимальный размер запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<ImageDTO>> uploadMultipleImages(
            @PathVariable @Parameter(description = "id объявления", example = "10") long id,
            @Parameter(
                    description = "Массив файлов фотографий",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary"))
            ) @RequestPart("files") MultipartFile[] files,
            Authentication authentication) {

        propertyService.ownsPropertyOrAdmin(id, authentication.getName());
        List<ImageDTO> savedImages = propertyService.uploadImages(files, id);

        return ResponseEntity.ok(savedImages);
    }


    @Operation(
            summary = "Получение всех объявлений",
            description = "Позволяет получить все объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyDTO[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<PropertyDTO>> getProperties() {
        List<PropertyDTO> properties = propertyService.findAll();
        return ResponseEntity.ok(properties);
    }

    @Operation(
            summary = "Получение всех объявлений, с фильтрацией",
            description = "Позволяет получить все объявления, с фильтрацией"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyDTO[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PostMapping(path = "/filtered/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PropertyDTO>> getPropertiesByFilter(@Valid @ModelAttribute PropertyFilterDTO propertyFilter) {
        List<PropertyDTO> properties = propertyService.findAllByFilter(propertyFilter);
        return ResponseEntity.ok(properties);
    }

    @Operation(
            summary = "Получение объявлений пользователя",
            description = "Позволяет получить все объявления пользователя по его токену. Только для авторизованных пользователей."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyDTO[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("/my")
    public ResponseEntity<List<PropertyDTO>> getUserProperties(Authentication authentication) {
        List<PropertyDTO> properties = propertyService.findAllByOwnerEmail(authentication.getName());
        return ResponseEntity.ok(properties);
    }

    @Operation(
            summary = "Получить объявление",
            description = "Получить объявление по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> findById(@Valid @PathVariable @Parameter(description = "id объявления", example = "10") long id) {
        PropertyDTO property = propertyService.findById(id);
        return ResponseEntity.ok(property);
    }

    @Operation(
            summary = "Создать объявление",
            description = "Создать объявление"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создано", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyDTO.class))
            }),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "JWT")
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyDTO> createPropertyWithImage(@Valid @ModelAttribute PropertyCreateEditDTO propertyCreateEditDTO, Authentication authentication) {
        propertyService.allowedToCreatePropertyOrAdmin(propertyCreateEditDTO, authentication.getName());
        return new ResponseEntity<>(propertyService.createWithImages(propertyCreateEditDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Изменить объявление",
            description = "Изменить объявление по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "JWT")
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyDTO> updateProperty(@Valid @PathVariable @Parameter(description = "id объявления", example = "1") long id,
                                                      @Valid @ModelAttribute PropertyCreateEditDTO propertyCreateEditDTO,
                                                      Authentication authentication) {
        propertyService.ownsPropertyOrAdmin(id, authentication.getName());
        return ResponseEntity.ok(propertyService.update(id, propertyCreateEditDTO));
    }

    @Operation(
            summary = "Удалить объявление",
            description = "Удалить объявление по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@Valid @PathVariable @Parameter(description = "id объявления", example = "10") long id, Authentication authentication) {
        if (propertyService.ownsPropertyOrAdmin(id, authentication.getName())) {
            propertyService.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }
}
