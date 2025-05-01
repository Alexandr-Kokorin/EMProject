package em.service.user;

import em.domain.entity.ApplicationUser;
import em.domain.entity.enums.GlobalPermissionName;
import em.domain.repository.ApplicationUserRepository;
import em.exception.PermissionDeniedException;
import em.exception.entity.not_found.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUtilService {

    private final ApplicationUserRepository userRepository;

    public ApplicationUser findUserByAuthentication(Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

    public void checkUserGlobalPermission(ApplicationUser user, GlobalPermissionName permission) {
        if (!user.getGlobalPermission().getName().equals(permission)) {
            throw new PermissionDeniedException(permission);
        }
    }

    public void checkUserGlobalPermission(Authentication authentication, GlobalPermissionName permission) {
        var user = findUserByAuthentication(authentication);
        checkUserGlobalPermission(user, permission);
    }
}
