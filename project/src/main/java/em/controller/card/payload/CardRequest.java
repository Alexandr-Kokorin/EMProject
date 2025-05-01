package em.controller.card.payload;

import em.domain.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на добавление новой карты")
public record CardRequest(
    @Schema(description = "Адрес электронной почты пользователя", example = "user@example.com")
    @Pattern(message = "{user.email.invalid}", regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank(message = "{user.mail.is_blank}")
    String email,

    @Schema(description = "Номер банковской карты (16 цифр)", example = "1234 5678 1234 5678")
    @Pattern(message = "{card.number.invalid}", regexp = "^(\\d{4}\\s){3}\\d{4}$")
    @NotBlank(message = "{card.number.is_blank}")
    String number,

    @Schema(description = "Срок действия карты", example = "2025-12-31")
    @NotNull(message = "{card.validity_period.is_null}")
    LocalDate validityPeriod,

    @Schema(description = "Статус карты", example = "ACTIVE", allowableValues = {"ACTIVE", "BLOCKED", "EXPIRED"})
    @NotNull(message = "{card.status.is_null}")
    CardStatus status,

    @Schema(description = "Баланс на карте", example = "10000.00")
    @NotNull(message = "{card.balance.is_null}")
    @DecimalMin(value = "0.00", message = "{card.balance.must_be_positive}")
    BigDecimal balance
) {
}
