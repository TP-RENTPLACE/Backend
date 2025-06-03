package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.rentplace.dtos.properties.PropertyDTO;
import kattsyn.dev.rentplace.services.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("${api.path}/favourites")
@RequiredArgsConstructor
@Tag(name = "FavouritesController", description = "Контроллер для работы с избранными объявлениями пользователя")
public class FavouritesController {

    private final FavouritesService favouritesService;

    @Operation(
            summary = "Получение избранных объявлений пользователем",
            description = "Метод, для получения избранных объявлений пользователем. Для авторизованных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyDTO[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("/")
    public ResponseEntity<List<PropertyDTO>> findAll(Authentication authentication) {
        return ResponseEntity.ok(favouritesService.getUserFavouritesByUserEmail(authentication.getName()));
    }

    @Operation(
            summary = "Добавить объявление в избранное"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Без тела ответа", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "401", description = "Не авторизован", content = @Content),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @PatchMapping(path = "/add/{id}")
    public ResponseEntity<Void> addPropertyToFavourites(@PathVariable long id, Authentication authentication) {
        favouritesService.addPropertyToFavourites(id, authentication.getName());

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить объявление из избранного"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Без тела ответа", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "401", description = "Не авторизован", content = @Content),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @PatchMapping(path = "/remove/{id}")
    public ResponseEntity<Void> removePropertyFromFavourites(@PathVariable long id, Authentication authentication) {
        favouritesService.removePropertyFromFavourites(id, authentication.getName());

        return ResponseEntity.ok().build();
    }

}
