package em.controller.card;

import em.controller.card.payload.CardRequest;
import em.controller.card.payload.CardResponse;
import em.controller.card.payload.TransferRequest;
import em.domain.entity.enums.CardStatus;
import em.service.card.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cards")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@Tag(name = "Карты", description = "API взаимодействия с картами")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Получить список всех карт",
               description = "Возвращает список всех карт")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение",
                     content = @Content(array = @ArraySchema(schema = @Schema(implementation = CardResponse.class)))),
        @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/me")
    public Page<CardResponse> findAllMe(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) CardStatus status,
        @RequestParam(defaultValue = "false") boolean sortByBalanceDesc
    ) {
        return cardService.findAllMe(authentication, page, size, status, sortByBalanceDesc);
    }

    @Operation(summary = "Получить список всех карт пользователя по email",
               description = "Возвращает список всех карт по email пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение",
                     content = @Content(array = @ArraySchema(schema = @Schema(implementation = CardResponse.class)))),
        @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь с указанным email не найден",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    @GetMapping
    public List<CardResponse> findAll(Authentication authentication, @RequestParam @NotBlank String email) {
        return cardService.findAll(authentication, email);
    }

    @Operation(summary = "Получить карту по id",
               description = "Возвращает карту")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение",
                     content = @Content(schema = @Schema(implementation = CardResponse.class))),
        @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Карта не найдена",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public CardResponse find(Authentication authentication, @PathVariable Long id) {
        return cardService.find(authentication, id);
    }

    @Operation(summary = "Создать новую карту",
               description = "Создает новую карту")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное создание"),
        @ApiResponse(responseCode = "400", description = "Неверный ввод",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь с указанным email не найден",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Карта уже существует",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public void createCard(Authentication authentication, @Valid @RequestBody CardRequest request) {
        cardService.createCard(authentication, request);
    }

    @Operation(summary = "Удалить карту",
               description = "Удаляет карту")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Успешное удаление",
                     content = @Content),
        @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Карта не найдена",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(Authentication authentication, @PathVariable Long id) {
        cardService.deleteCard(authentication, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить статус карты",
               description = "Обновляет статус карты")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное создание"),
        @ApiResponse(responseCode = "400", description = "Неверный ввод",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь с указанным email не найден",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Карта уже существует",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}")
    public void updateStatus(Authentication authentication, @RequestParam CardStatus status, @PathVariable Long id) {
        cardService.updateStatus(authentication, status, id);
    }

    @Operation(summary = "Совершить перевод с одной карты на другую",
               description = "Переводит деньги с одной карты на другую")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешный перевод"),
        @ApiResponse(responseCode = "400", description = "Неверный ввод",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Ошибка аутентификации",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Карта не найдена",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Недостаточно средств",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/transfer")
    public void makeTransfer(Authentication authentication, @Valid @RequestBody TransferRequest request) {
        cardService.makeTransfer(authentication, request);
    }
}
