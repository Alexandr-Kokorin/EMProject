package em.service.card;

import em.controller.card.payload.CardRequest;
import em.controller.card.payload.CardResponse;
import em.controller.card.payload.TransferRequest;
import em.domain.entity.BankCard;
import em.domain.entity.enums.CardStatus;
import em.domain.entity.enums.GlobalPermissionName;
import em.domain.repository.ApplicationUserRepository;
import em.domain.repository.BankCardRepository;
import em.exception.InsufficientFundsException;
import em.exception.entity.already_exists.CardAlreadyExistsException;
import em.exception.entity.not_found.CardNotFoundException;
import em.exception.entity.not_found.UserNotFoundException;
import em.service.card.mapper.CardMapper;
import em.service.user.UserUtilService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("MultipleStringLiterals")
@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final UserUtilService userUtilService;
    private final AesEncryptionService aesEncryptionService;

    private final BankCardRepository cardRepository;
    private final ApplicationUserRepository userRepository;

    private final CardMapper mapper;

    public Page<CardResponse> findAllMe(
        Authentication authentication,
        int pageNum,
        int pageSize,
        CardStatus status,
        boolean sortByBalanceDesc
    ) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.USER);
        var user = userUtilService.findUserByAuthentication(authentication);

        Sort sort = sortByBalanceDesc
            ? Sort.by("balance").descending()
            : Sort.by("balance").ascending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<BankCard> page = (status == null)
            ? cardRepository.findAllByApplicationUser(user, pageable)
            : cardRepository.findAllByApplicationUserAndStatus(user, status, pageable);

        return page.map(card -> mapper.entityToResponse(card, aesEncryptionService));
    }

    public List<CardResponse> findAll(Authentication authentication, String email) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return user.getBankCards().stream()
            .map(card -> mapper.entityToResponse(card, aesEncryptionService))
            .toList();
    }

    public CardResponse find(Authentication authentication, Long id) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        var card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return mapper.entityToResponse(card, aesEncryptionService);
    }

    public void createCard(Authentication authentication, CardRequest request) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        var user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException(request.email()));
        if (cardRepository.findByNumber(request.number()).isPresent()) {
            throw new CardAlreadyExistsException();
        }

        var card = mapper.requestToEntity(request, aesEncryptionService);
        card.setApplicationUser(user);

        cardRepository.save(card);
    }

    public void deleteCard(Authentication authentication, Long id) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        var card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        cardRepository.delete(card);
    }

    public void updateStatus(Authentication authentication, CardStatus status, Long id) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        var card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        card.setStatus(status);

        cardRepository.save(card);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void makeTransfer(Authentication authentication, TransferRequest request) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.USER);

        var cardFrom = cardRepository.findById(request.cardFrom())
            .orElseThrow(() -> new CardNotFoundException(request.cardFrom()));
        var cardTo = cardRepository.findById(request.cardTo())
            .orElseThrow(() -> new CardNotFoundException(request.cardTo()));
        if (cardFrom.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException();
        }

        cardFrom.setBalance(cardFrom.getBalance().subtract(request.amount()));
        cardTo.setBalance(cardTo.getBalance().add(request.amount()));

        cardRepository.save(cardFrom);
        cardRepository.save(cardTo);
    }
}
