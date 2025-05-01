package em.service.user;

import em.controller.user.payload.UserResponse;
import em.controller.user.payload.UserUpdateRequest;
import em.domain.entity.ApplicationUser;
import em.domain.entity.enums.GlobalPermissionName;
import em.domain.repository.ApplicationUserRepository;
import em.exception.entity.not_found.UserNotFoundException;
import em.service.user.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserUtilService userUtilService;

    private final ApplicationUserRepository userRepository;
    private final UserMapper mapper;

    public List<UserResponse> findAllUsers(Authentication authentication) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        List<ApplicationUser> users = userRepository.findAll();
        return users.stream()
            .map(mapper::entityToResponse)
            .toList();
    }

    public UserResponse findUser(Authentication authentication, String email) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
        return mapper.entityToResponse(user);
    }

    public UserResponse getCurrentUser(Authentication authentication) {
        var currentUser = userUtilService.findUserByAuthentication(authentication);
        return mapper.entityToResponse(currentUser);
    }

    public UserResponse updateUser(Authentication authentication, UserUpdateRequest request) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        var userToUpdate = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException(request.email()));

        userToUpdate.setHashedPassword(passwordEncoder.encode(request.password()));
        userToUpdate.setDisplayName(request.displayName());

        return mapper.entityToResponse(userRepository.save(userToUpdate));
    }

    public void deleteUser(Authentication authentication, String email) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        userRepository.delete(userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email)));
    }
}
