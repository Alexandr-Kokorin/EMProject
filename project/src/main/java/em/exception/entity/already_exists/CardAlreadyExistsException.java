package em.exception.entity.already_exists;

import em.exception.base.ApplicationConflictException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class CardAlreadyExistsException extends ApplicationConflictException {

    public CardAlreadyExistsException() {
        super("card.number.is_busy", new Object[]{});
    }
}
