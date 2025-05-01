package em.service.security;

import em.controller.security.payload.AuthenticationRequest;
import em.controller.security.payload.AuthenticationResponse;
import em.controller.security.payload.RegisterRequest;
import em.domain.entity.enums.GlobalPermissionName;
import em.domain.repository.ApplicationUserRepository;
import em.domain.repository.GlobalPermissionRepository;
import em.exception.entity.already_exists.UserAlreadyExistsException;
import em.service.security.mapper.AuthenticationMapper;
import em.service.user.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuthenticationMapper mapper;


    public void registerUser(RegisterRequest request, Authentication authentication) {
        userUtilService.checkUserGlobalPermission(authentication, GlobalPermissionName.ADMIN);

        if (appUserRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException(request.email());
        }

        var user = mapper.requestToEntity(request, passwordEncoder);
        user.setGlobalPermission(globalPermissionRepository.findByName(GlobalPermissionName.USER));

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
}
