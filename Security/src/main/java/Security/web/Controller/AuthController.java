package Security.web.Controller;

import Security.config.AppProperties;
import Security.db.model.EnumsType.AuthProvider;
import Security.db.model.UserEntity;
import Security.db.repository.UserRepository;
import Security.exceptions.BadRequestException;
import Security.security.TokenProvider;
import Security.service.CacheService;
import Security.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CacheService cacheService;
    @Autowired
    AppProperties appProperties;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        UserEntity user = new UserEntity();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());

        user.setProvider(AuthProvider.local);
        UserEntity result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

    @PostMapping("/logout")
    public ResponseEntity logoutUSer(HttpServletRequest request) {
        cacheService.logOutUSerFromCache(appProperties.getJwtFromRequest(request));
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/data")
    public UserDataForAnotherApp getUserFromTokenFromAnotherApp(HttpServletRequest request) {

        return cacheService.getUserFromToken(appProperties.getJwtFromRequest(request));
    }
}