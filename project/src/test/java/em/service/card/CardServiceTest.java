package em.service.card;

import em.controller.card.payload.CardRequest;
import em.controller.card.payload.TransferRequest;
import em.domain.entity.ApplicationUser;
import em.domain.entity.BankCard;
import em.domain.entity.enums.CardStatus;
import em.domain.entity.enums.GlobalPermissionName;
import em.domain.repository.ApplicationUserRepository;
import em.domain.repository.BankCardRepository;
import em.service.card.mapper.CardMapper;
import em.service.user.UserUtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock private UserUtilService userUtilService;
    @Mock private AesEncryptionService aesEncryptionService;
    @Mock private BankCardRepository cardRepository;
    @Mock private ApplicationUserRepository userRepository;
    @Mock private CardMapper cardMapper;
    @Mock private Authentication authentication;

    @InjectMocks
    private CardService cardService;

    @Test
    void findAll_shouldReturnCardsByAdminForUser() {
        ApplicationUser user = new ApplicationUser();
        BankCard card = new BankCard();
        user.setBankCards(List.of(card));

        Mockito.when(userRepository.findByEmail("email")).thenReturn(Optional.of(user));

        var result = cardService.findAll(authentication, "email");

        assertEquals(1, result.size());
        verify(userUtilService).checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);
    }

    @Test
    void createCard_shouldSaveNewCard() {
        CardRequest request = new CardRequest("email", "1234567890123456", LocalDate.now(), CardStatus.ACTIVE, BigDecimal.TEN);
        ApplicationUser user = new ApplicationUser();
        BankCard card = new BankCard();

        Mockito.when(userRepository.findByEmail("email")).thenReturn(Optional.of(user));
        Mockito.when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.empty());
        Mockito.when(cardMapper.requestToEntity(request, aesEncryptionService)).thenReturn(card);

        cardService.createCard(authentication, request);

        verify(cardRepository).save(card);
        assertEquals(user, card.getApplicationUser());
    }

    @Test
    void updateStatus_shouldChangeCardStatus() {
        BankCard card = new BankCard();
        card.setStatus(CardStatus.ACTIVE);

        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        cardService.updateStatus(authentication, CardStatus.BLOCKED, 1L);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void makeTransfer_shouldTransferFunds() {
        BankCard from = new BankCard();
        from.setBalance(BigDecimal.valueOf(100));
        BankCard to = new BankCard();
        to.setBalance(BigDecimal.ZERO);
        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(50));

        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(from));
        Mockito.when(cardRepository.findById(2L)).thenReturn(Optional.of(to));

        cardService.makeTransfer(authentication, request);

        assertEquals(BigDecimal.valueOf(50), from.getBalance());
        assertEquals(BigDecimal.valueOf(50), to.getBalance());
        verify(cardRepository).save(from);
        verify(cardRepository).save(to);
    }
}

