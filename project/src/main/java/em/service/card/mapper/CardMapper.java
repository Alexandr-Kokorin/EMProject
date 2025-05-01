package em.service.card.mapper;

import em.controller.card.payload.CardRequest;
import em.controller.card.payload.CardResponse;
import em.domain.entity.BankCard;
import em.service.card.AesEncryptionService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@SuppressWarnings("MagicNumber")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {

    @Mapping(target = "number", expression = "java(encrypt(request.number(), aesService))")
    BankCard requestToEntity(CardRequest request, @Context AesEncryptionService aesService);

    @Mapping(target = "number", expression = "java(decrypt(card.getNumber(), aesService))")
    CardResponse entityToResponse(BankCard card, @Context AesEncryptionService aesService);

    default String encrypt(String raw, @Context AesEncryptionService aesService) {
        try {
            return aesService.encrypt(raw);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при шифровании номера карты", e);
        }
    }

    default String decrypt(String encrypted, @Context AesEncryptionService aesService) {
        try {
            var tmp = aesService.decrypt(encrypted);
            return "**** **** **** " + tmp.substring(tmp.length() - 4);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при дешифровании номера карты", e);
        }
    }
}
