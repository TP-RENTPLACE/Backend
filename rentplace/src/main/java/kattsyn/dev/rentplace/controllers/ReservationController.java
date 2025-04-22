package kattsyn.dev.rentplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.rentplace.dtos.*;
import kattsyn.dev.rentplace.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/reservations")
@Tag(name = "Reservation Controller", description = "Взаимодействие с бронированиями")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(
            summary = "Получение всех бронирований",
            description = "Позволяет получить все бронирования"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDTO[].class))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        List<ReservationDTO> reservationDTOS = reservationService.findAllReservations();
        return ResponseEntity.ok(reservationDTOS);
    }

    @Operation(
            summary = "Получение бронирования",
            description = "Получение бронирования по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable
                                                         @Parameter(description = "id бронирования", example = "1") long id) {
        ReservationDTO reservationDTO = reservationService.getReservationDTOById(id);
        return ResponseEntity.ok(reservationDTO);
    }

    @Operation(
            summary = "Создать бронирование",
            description = "Создать бронирование"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создано", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDTO.class))
            }),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReservationDTO> createReservation(@ModelAttribute ReservationCreateEditDTO reservationCreateEditDTO) {
        return ResponseEntity.ok(reservationService.createReservation(reservationCreateEditDTO));
    }

    @Operation(
            summary = "Изменить бронирование",
            description = "Изменить бронирование"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно изменено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDTO.class))
            }),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable @Parameter(description = "id бронирования для изменения") long id,
                                                            @ModelAttribute ReservationCreateEditDTO reservationCreateEditDTO) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservationCreateEditDTO));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно. Возвращает удаленную сущность", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Получен некорректный ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации", content = @Content),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка со стороны сервера", content = @Content)
    })
    public ResponseEntity<ReservationDTO> deleteReservation(
            @PathVariable
            @Parameter(description = "id бронирования", example = "1") long id
    ) {
        return ResponseEntity.ok(reservationService.deleteById(id));
    }

}
