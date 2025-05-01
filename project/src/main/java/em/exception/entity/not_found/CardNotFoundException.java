package em.exception.entity.not_found;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class CardNotFoundException extends EntityNotFoundException {
    public CardNotFoundException(Long id) {
        super("card.id.not.found", id);
    }
}
