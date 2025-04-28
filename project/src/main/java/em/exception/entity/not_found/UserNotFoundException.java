package em.exception.entity.not_found;

import em.exception.base.ApplicationNotFoundException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class UserNotFoundException extends ApplicationNotFoundException {

    public UserNotFoundException(String email) {
        super("user.email.not_found", new Object[]{email});
    }
}
