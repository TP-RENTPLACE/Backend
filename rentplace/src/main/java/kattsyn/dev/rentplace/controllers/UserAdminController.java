package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kattsyn.dev.rentplace.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/${api.path}/users/admin")
@Tag(name = "UserAdminController", description = "Для взаимодействия с пользователями из админ-панели")
public class UserAdminController {

    private final UserService userService;

    @Operation(
            summary = "Удалить пользователя",
            description = "Удалить пользователя по ID. Использовать в крайних случаях. При удалении удаляются все его брони и объявления."
    )
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @Valid @Parameter(description = "id пользователя", example = "1") long id
    ) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Заблокировать пользователя",
            description = "Заблокировать пользователя по ID. В случае нарушений правил пользования сервисом."
    )
    @GetMapping("/block-user/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> blockUser(
            @PathVariable
            @Valid @Parameter(description = "id пользователя", example = "1") long id
    ) {
        userService.blockUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Разблокировать/активировать пользователя",
            description = "Разблокировать/активировать пользователя по ID. В случае снятия блокировки или если пользователь захочет разблокировать свой профиль."
    )
    @GetMapping("/activate-user/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> activateUser(
            @PathVariable
            @Valid @Parameter(description = "id пользователя", example = "1") long id
    ) {
        userService.activateUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Заблокировать пользователя",
            description = "Заблокировать пользователя по ID. В случае нарушений правил пользования сервисом."
    )
    @GetMapping("/deactivate-user/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно. Пустой ответ", content = @Content),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable
            @Valid @Parameter(description = "id пользователя", example = "1") long id
    ) {
        userService.deactivateUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
