package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationRequest;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationResponse;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

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
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }catch (AuthenticationException exception){
            logger.error(exception.getMessage());
            authResponse.setMessage(exception.getMessage());
            logger.info("***** AUTHENTICATION END *****");
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        }

    }

}
