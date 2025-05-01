package em.service.user.mapper;

import em.controller.user.payload.UserResponse;
import em.domain.entity.ApplicationUser;
import em.domain.entity.GlobalPermission;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", source = "globalPermission", qualifiedByName = "globalPermissionToRole")
    UserResponse entityToResponse(ApplicationUser user);

    @Named("globalPermissionToRole")
    static String globalPermissionToRole(@NotNull GlobalPermission globalPermission) {
        return globalPermission.getName().name();
    }
}
