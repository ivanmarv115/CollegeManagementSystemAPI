package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationRequest;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationResponse;
import ivanmartinez.simpleStudentsAPI.DTO.ChangePasswordRequest;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
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
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow();

            String jwt = jwtService.generateToken(user);
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

    public User getUser() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userByUsername = userRepository.findByUsername(username);

        if(userByUsername.isEmpty()){
            throw new ResourceNotFoundException("No se pudo identificar al usuario logeado");
        }

        return userByUsername.get();

    }

}
