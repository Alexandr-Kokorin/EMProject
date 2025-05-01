package em.exception;

import em.domain.entity.enums.GlobalPermissionName;
import em.exception.base.ApplicationForbiddenException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class PermissionDeniedException extends ApplicationForbiddenException {

    public PermissionDeniedException(GlobalPermissionName permission) {
        super("user.admin.only_access", new Object[]{permission});
    }
}
