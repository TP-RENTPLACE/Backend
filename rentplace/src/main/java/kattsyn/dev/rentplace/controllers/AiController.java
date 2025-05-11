package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import kattsyn.dev.rentplace.dtos.ai.GenerateDescriptionRequest;
import kattsyn.dev.rentplace.dtos.ai.GenerateDescriptionResponse;
import kattsyn.dev.rentplace.services.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}/ai")
@RequiredArgsConstructor
@Tag(name = "AI Controller", description = "Интеграция с OpenRouter для работы с LLM моделями")
public class AiController {

    private final AiService aiService;

    @Operation(
            summary = "Сгенерировать описание проекта",
            description = "Принимает системный и пользовательский промпты, отсылает их в Open Router AI и возвращает сгенерированный текст."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сгенерированное описание получено"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "429", description = "Превышен лимит AI-запросов (10 в час)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping(path = "/description", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<GenerateDescriptionResponse> generateDescription(
            @Valid @ModelAttribute GenerateDescriptionRequest request
    ) throws AuthException {
        GenerateDescriptionResponse response = new GenerateDescriptionResponse(aiService.generateDescription(request));
        return ResponseEntity.ok(response);
    }

}
