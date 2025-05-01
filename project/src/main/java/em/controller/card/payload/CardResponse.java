package em.controller.card.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import em.domain.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "Ответ, содержащий данные карты")
public record CardResponse(
    @Schema(description = "ID банковской карты", example = "1")
    Long id,

    @Schema(description = "Номер банковской карты", example = "**** **** **** 5678")
    String number,

    @Schema(description = "Срок действия карты", example = "2025-12-31")
    @JsonProperty("validity_period")
    LocalDate validityPeriod,

    @Schema(description = "Статус карты", example = "ACTIVE")
    CardStatus status,

    @Schema(description = "Баланс на карте", example = "10000.00")
    BigDecimal balance
) {
}
