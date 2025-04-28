package em.service.users;

import em.controller.users.payload.UserResponse;
import em.controller.users.payload.UserUpdateRequest;
import em.domain.entity.ApplicationUser;
import em.domain.entity.enums.GlobalPermissionName;
import em.domain.repository.ApplicationUserRepository;
import em.exception.entity.not_found.UserNotFoundException;
import em.service.security.AuthenticationService;
import em.service.users.mapper.UserMapper;
import em.service.util.UserUtilService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserService {

    private final AuthenticationService authService;
    private final UserUtilService userUtilService;

    private final ApplicationUserRepository userRepository;
    private final UserMapper mapper;

    public List<UserResponse> findAllUsers() {
        List<ApplicationUser> users = userRepository.findAll();
        return users.stream()
            .map(mapper::entityToResponse)
            .toList();
    }

    public UserResponse findUser(String email) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
        return mapper.entityToResponse(user);
    }

    public UserResponse getCurrentUser(Authentication authentication) {
        var currentUser = userUtilService.findUserByAuthentication(authentication);

        return mapper.entityToResponse(currentUser);
    }

    public UserResponse updateUser(Authentication authentication, UserUpdateRequest updateRequest) {
        var userToUpdate = userUtilService.findUserByAuthentication(authentication);

        userToUpdate.setHashedPassword(authService.encodePassword(updateRequest.password()));
        userToUpdate.setDisplayName(updateRequest.displayName());

        return mapper.entityToResponse(userRepository.save(userToUpdate));
    }

    public void deleteUser(Authentication authentication, String email) {
        userUtilService.checkUserGlobalPermission(
            userUtilService.findUserByAuthentication(authentication), GlobalPermissionName.ADMIN);

        userRepository.delete(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email)));
    }
}
