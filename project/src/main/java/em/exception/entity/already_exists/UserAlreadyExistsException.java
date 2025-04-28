package em.exception.entity.already_exists;

import em.exception.base.ApplicationConflictException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class UserAlreadyExistsException extends ApplicationConflictException {

    public UserAlreadyExistsException(String email) {
        super("user.email.is_busy", new Object[]{email});
    }
}
