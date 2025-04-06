package kattsyn.dev.rentplace.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.rentplace.dtos.FacilityDTO;
import kattsyn.dev.rentplace.entities.Facility;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.services.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("${api.path}/facilities")
@RestController
@RequiredArgsConstructor
@Tag(name = "FacilityController", description = "Для взаимодействия с удобствами")
public class FacilityController {

    private final FacilityService facilityService;

    @Operation(
            summary = "Загрузка фотографии для категории",
            description = "Загрузка фотографии для категории"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> uploadImage(
            @Parameter(
                    description = "Файл фотографии",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) @RequestParam("file") MultipartFile file,
            @PathVariable
            @Parameter(description = "id категории", example = "10") long id) {

        return ResponseEntity.ok(facilityService.uploadImage(file, id));
    }

    @Operation(summary = "Получение всех удобств", description = "Получение всех удобств")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Facility[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<Facility>> findAll() {
        return ResponseEntity.ok(facilityService.findAll());
    }

    @Operation(
            summary = "Получить удобство",
            description = "Получить удобство по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Facility.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Удобство не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Facility> findById(
            @PathVariable
            @Parameter(description = "id удобства", example = "2") long id) {
        return ResponseEntity.ok(facilityService.findById(id));
    }


    @Operation(
            summary = "Создать удобство",
            description = "Создать удобство"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создано", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FacilityDTO.class))
            }),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Facility> createFacility(FacilityDTO facilityDTO) {
        return ResponseEntity.ok(facilityService.save(facilityDTO));
    }

    @Operation(
            summary = "Изменить удобство",
            description = "Изменить удобство по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Удобство не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Facility> updateFacility(
            @PathVariable
            @Parameter(description = "id удобства", example = "10") long id,
            @RequestBody FacilityDTO facilityDTO) {
        return ResponseEntity.ok(facilityService.update(id, facilityDTO));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Удобство не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    public ResponseEntity<Facility> deleteFacility(
            @PathVariable
            @Parameter(description = "id удобства", example = "10") long id
    ) {
        return ResponseEntity.ok(facilityService.deleteById(id));
    }
}
