package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Property;
import kattsyn.dev.rentplace.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/properties")
@Tag(name = "Property Controller", description = "Взаимодействие с имуществом")
public class PropertyController {

    private final PropertyService propertyService;

    @Operation(
            summary = "Получение всех объявлений",
            description = "Позволяет получить все объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<Property>> getProperties() {
        List<Property> properties = propertyService.findAll();
        return ResponseEntity.ok(properties);
    }

    @Operation(
            summary = "Получить объявление",
            description = "Получить объявление по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Property.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Property> findById(@PathVariable @Parameter(description = "id объявления", example = "10") long id) {
        Property property = propertyService.findById(id);
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
    @PostMapping("/")
    public ResponseEntity<Property> createProperty(@RequestBody PropertyDTO propertyDTO) {
        return new ResponseEntity<>(propertyService.save(propertyDTO), HttpStatus.CREATED);
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
    @PatchMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable @Parameter(description = "id объявления", example = "10") long id,
                                                   @RequestBody PropertyDTO propertyDTO) {
        return ResponseEntity.ok(propertyService.update(id, propertyDTO));
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Property> deleteProperty(@PathVariable @Parameter(description = "id объявления", example = "10") long id) {
        return new ResponseEntity<>(propertyService.deleteById(id), HttpStatus.NO_CONTENT);
    }


}
