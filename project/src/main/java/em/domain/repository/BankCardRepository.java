package em.domain.repository;

import em.domain.entity.ApplicationUser;
import em.domain.entity.BankCard;
import em.domain.entity.enums.CardStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCardRepository extends JpaRepository<BankCard, Long> {

    Optional<BankCard> findByNumber(String number);

    Page<BankCard> findAllByApplicationUserAndStatus(ApplicationUser user, CardStatus status, Pageable pageable);

    Page<BankCard> findAllByApplicationUser(ApplicationUser user, Pageable pageable);
}
