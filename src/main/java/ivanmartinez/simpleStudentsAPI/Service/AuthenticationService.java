package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationRequest;
import ivanmartinez.simpleStudentsAPI.DTO.AuthenticationResponse;
import ivanmartinez.simpleStudentsAPI.DTO.RegisterRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<AuthenticationResponse> register(
            RegisterRequest request
    ) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var authResponse = AuthenticationResponse.builder()
                .token(jwt)
                .build();

        return new ResponseEntity<AuthenticationResponse>
                (authResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<AuthenticationResponse> authenticate
            (AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        var jwt = jwtService.generateToken(user);
        var authResponse = AuthenticationResponse.builder()
                .token(jwt)
                .build();

        return new ResponseEntity<AuthenticationResponse>(authResponse,
                HttpStatus.OK);
    }
}
