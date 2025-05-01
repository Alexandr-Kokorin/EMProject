package em.service.security.mapper;

import em.controller.security.payload.RegisterRequest;
import em.domain.entity.ApplicationUser;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthenticationMapper {

    @Mapping(target = "hashedPassword", ignore = true)
    ApplicationUser requestToEntity(RegisterRequest request);

    default ApplicationUser requestToEntity(RegisterRequest request, @Context PasswordEncoder encoder) {
        var user = requestToEntity(request);
        user.setHashedPassword(encoder.encode(request.password()));
        return user;
    }
}
