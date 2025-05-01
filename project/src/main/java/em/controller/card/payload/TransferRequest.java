package em.controller.card.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на перевод средств")
public record TransferRequest(
    @Schema(description = "ID карты отправителя", example = "1")
    @NotNull(message = "{transfer.card_from.is_null}")
    Long cardFrom,

    @Schema(description = "ID карты получателя", example = "2")
    @NotNull(message = "{transfer.card_to.is_null}")
    Long cardTo,

    @Schema(description = "Сумма перевода", example = "500.00")
    @NotNull(message = "{transfer.amount.is_null}")
    @DecimalMin(value = "0.01", message = "{transfer.amount.must_be_positive}")
    BigDecimal amount
) {
}
