package em.controller.security.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Ответ аутентификации, содержащий JWT токен")
public record AuthenticationResponse(
    @Schema(description = "JWT токен для аутентифицированного пользователя",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken
) {
}
