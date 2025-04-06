package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.rentplace.dtos.CategoryDTO;
import kattsyn.dev.rentplace.entities.Category;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/categories")
@Tag(name = "CategoryController", description = "Для взаимодействия с категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Получение всех категорий",
            description = "Получение всех категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<Category>> findAll() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Получение категории",
            description = "Получение категории по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(
            @PathVariable
            @Parameter(description = "id категории", example = "10") long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(
            summary = "Создать категорию",
            description = "Создать категорию"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создано", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
            }),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Category> createCategory(CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.save(categoryDTO));
    }

    @Operation(
            summary = "Изменить категорию",
            description = "Изменить категорию по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable
            @Parameter(description = "id категории", example = "10") long id,
            @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.update(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    public ResponseEntity<Category> deleteCategory(
            @PathVariable
            @Parameter(description = "id категории", example = "10") long id
    ) {
        return ResponseEntity.ok(categoryService.deleteById(id));
    }

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
        Image image = categoryService.uploadImage(file, id);
        return ResponseEntity.ok(image);
    }
}
