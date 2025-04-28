package em.service.security;

import em.controller.secutiry.payload.AuthenticationRequest;
import em.controller.secutiry.payload.AuthenticationResponse;
import em.controller.secutiry.payload.RegisterRequest;
import em.domain.entity.ApplicationUser;
import em.domain.entity.enums.GlobalPermissionName;
import em.domain.repository.ApplicationUserRepository;
import em.domain.repository.GlobalPermissionRepository;
import em.exception.entity.already_exists.UserAlreadyExistsException;
import em.service.util.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static em.domain.entity.enums.GlobalPermissionName.ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserUtilService userUtilService;

    private final GlobalPermissionRepository globalPermissionRepository;
    private final ApplicationUserRepository appUserRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequest request, Authentication authentication) {
        var admin = userUtilService.findUserByAuthentication(authentication);
        userUtilService.checkUserGlobalPermission(admin, ADMIN);
        var permission = GlobalPermissionName.USER;

        checkEmail(request.email());
        var globalPermission = globalPermissionRepository.findByName(permission);

        var user = ApplicationUser.builder()
            .email(request.email())
            .displayName(request.displayName())
            .globalPermission(globalPermission)
            .hashedPassword(encodePassword(request.password()))
            .build();

        appUserRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            ));

        var user = appUserRepository.findByEmail(request.email()).orElseThrow();
        var accessToken = jwtService.generateToken(user);

        return new AuthenticationResponse(accessToken);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void checkEmail(String email) {
        var applicationUser = appUserRepository.findByEmail(email);
        if (applicationUser.isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
    }
}
