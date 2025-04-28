package em.exception.entity.not_found;

import em.exception.base.ApplicationNotFoundException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class TokenNotFoundException extends ApplicationNotFoundException {
    public TokenNotFoundException(String token) {
        super("token.not_found", new String[]{token});
    }
}
