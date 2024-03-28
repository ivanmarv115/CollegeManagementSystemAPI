package ivanmartinez.simpleStudentsAPI.Service.Implementations;

import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public ResponseEntity<String> addUser(CreateUserRequest createUserRequest)
            throws ResourceAlreadyExistsException {
        logger.info("***** ADD NEW USER *****");
        logger.info("Request: " + createUserRequest);
        createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    public User createUser(CreateUserRequest createUserRequest)
            throws ResourceAlreadyExistsException {
        Optional<User> userByUsername = userRepository.findByUsername(
                createUserRequest.getUsername()
        );
        if (userByUsername.isPresent()){
            logger.warn("Username already exists");
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        User user = createUserRequestToUserEntity(createUserRequest);
        userRepository.save(user);
        logger.info("New user created");
        return user;
    }

    private User createUserRequestToUserEntity(CreateUserRequest createUserRequest){
        return User.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .role(createUserRequest.getRole())
                .build();
    }

    @Override
    public ResponseEntity<String> lockUser(LongIdRequest request) throws ResourceNotFoundException {
        logger.info("***** LOCK USER *****");
        Optional<User> userOptional = userRepository.findById(request.getLongId());
        if (userOptional.isEmpty()){
            logger.warn("User not found");
            throw new ResourceNotFoundException("User not found");
        }
        User user = userOptional.get();
        user.setIsNonLocked(true);
        userRepository.save(user);
        logger.info("***** USER LOCKED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("User locked");
    }

    @Override
    public ResponseEntity<String> unlockUser(LongIdRequest request) throws ResourceNotFoundException {
        logger.info("***** UNLOCK USER *****");
        Optional<User> userOptional = userRepository.findById(request.getLongId());
        if (userOptional.isEmpty()){
            logger.warn("User not found");
            throw new ResourceNotFoundException("User not found");
        }
        User user = userOptional.get();
        user.setIsNonLocked(false);
        userRepository.save(user);
        logger.info("***** USER UNLOCKED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("User unlocked");
    }


}
