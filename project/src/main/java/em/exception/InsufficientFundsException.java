package em.exception;

import em.exception.base.ApplicationConflictException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class InsufficientFundsException extends ApplicationConflictException {

    public InsufficientFundsException() {
        super("insufficient.funds", new Object[]{});
    }
}
