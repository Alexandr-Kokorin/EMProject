package em.controller.card;

import em.controller.BaseControllerTest;
import em.controller.security.payload.AuthenticationRequest;
import em.controller.security.payload.AuthenticationResponse;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerTest extends BaseControllerTest {

    private static AuthenticationResponse authToken;
    private final String URL = "/api/v1/cards";

    @SneakyThrows
    private AuthenticationResponse login() {
        if (authToken != null) {
            return authToken;
        }

        var request = AuthenticationRequest.builder()
            .email("admin@gmail.com")
            .password("admin321@&123")
            .build();

        var mvcResponse = mockMvc.perform(post("/api/v1/auth/authenticate")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        authToken = objectMapper.readValue(
            mvcResponse.getResponse().getContentAsString(),
            AuthenticationResponse.class
        );

        return authToken;
    }

    // По аналогии с тестами пользователя
}
