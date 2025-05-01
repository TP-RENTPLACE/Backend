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
import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.dtos.UserCreateEditDTO;
import kattsyn.dev.rentplace.dtos.UserDTO;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("${api.path}/users")
@Tag(name = "UserController", description = "Для взаимодействия с пользователями")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Загрузка фотографии для пользователя",
            description = "Загрузка фотографии для пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')" )
    @SecurityRequirement(name = "JWT")
    @PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> uploadImage(
            @Parameter(
                    description = "Файл фотографии",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) @RequestParam("file") MultipartFile file,
            @PathVariable
            @Parameter(description = "id пользователя", example = "10") long id) {

        return ResponseEntity.ok(userService.uploadImage(file, id));
    }

    @Operation(summary = "Получение всех пользователей", description = "Получение всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(
            summary = "Получить пользователя",
            description = "Получить dto пользователя по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(
            @PathVariable
            @Valid @Parameter(description = "id пользователя", example = "1") long id) {
        return ResponseEntity.ok(userService.findById(id));
    }


    @Operation(
            summary = "Создать пользователя с аватаркой",
            description = "Создать пользователя с аватаркой"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создано", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
            }),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" )
    @SecurityRequirement(name = "JWT")
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> createUser(@ModelAttribute @Valid UserCreateEditDTO userCreateEditDTO) {
        return new ResponseEntity<>(userService.createWithImage(userCreateEditDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Изменить пользователя",
            description = "Изменить пользователя по id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')" )
    @SecurityRequirement(name = "JWT")
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable
            @Parameter(description = "id пользователя", example = "1") long id,
            @ModelAttribute @Valid UserCreateEditDTO userCreateEditDTO) {
        return ResponseEntity.ok(userService.update(id, userCreateEditDTO));
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удалить пользователя по ID"
    )
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')" )
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @Valid @Parameter(description = "id пользователя", example = "1") long id
    ) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
