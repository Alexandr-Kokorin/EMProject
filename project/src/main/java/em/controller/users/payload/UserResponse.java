package em.controller.users.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "Ответ на запрос, содержащий информацию о пользователе")
public record UserResponse(
    @JsonProperty("email")
    @Schema(description = "Адрес электронной почты пользователя", example = "user@example.com")
    String email,
    @JsonProperty("display_name")
    @Schema(description = "Отображаемое имя пользователя", example = "Иван Иванов")
    String displayName,
    @JsonProperty("role")
    @Schema(description = "Роль пользователя", example = "[\"USER\"]")
    String role
) {
}
