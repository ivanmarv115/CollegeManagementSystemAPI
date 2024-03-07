package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationRequest;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationResponse;
import ivanmartinez.simpleStudentsAPI.DTO.RegisterRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public ResponseEntity<AuthenticationResponse> register(
            RegisterRequest request
    ) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var authResponse = AuthenticationResponse.builder()
                .role(request.getRole())
                .token(jwt)
                .build();

        return new ResponseEntity<AuthenticationResponse>
                (authResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<AuthenticationResponse> authenticate
            (AuthenticationRequest request) {
        logger.info("***** AUTHENTICATION BEGIN *****");
        AuthenticationResponse authResponse = new AuthenticationResponse();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow();

            var jwt = jwtService.generateToken(user);
            authResponse.setRole(user.getRole());
            authResponse.setToken(jwt);

            logger.info("Authentication OK");
            logger.info("***** AUTHENTICATION END *****");
            return new ResponseEntity<AuthenticationResponse>(authResponse,
                    HttpStatus.OK);
        }catch (AuthenticationException exception){
            logger.error(exception.getMessage());
            authResponse.setMessage(exception.getMessage());
            logger.info("***** AUTHENTICATION END *****");
            return new ResponseEntity<AuthenticationResponse>(authResponse,
                    HttpStatus.UNAUTHORIZED);
        }

    }
}
