package ivanmartinez.simpleStudentsAPI.Service.Implementations;

import ivanmartinez.simpleStudentsAPI.DTO.ChangePasswordRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Professors.GetProfessorResponse;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import ivanmartinez.simpleStudentsAPI.Service.AuthenticationService;
import ivanmartinez.simpleStudentsAPI.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
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
        user.setIsNonLocked(true);
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
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("***** GET ALL USERS *****");
        List<User> users = userRepository.findAll();
        logger.info("***** OK *****");
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    public ResponseEntity<List<User>> getUsersContaining(GetByRequest request) throws ResourceNotFoundException {
        logger.info("***** GET USERS CONTAINING *****");
        logger.info("Request: " + request);

        List<User> response = new ArrayList<>();
        if(request.getId() != null){
            Optional<User> userOptional = userRepository.findById(request.getId());
            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("User not found");
            }
            response.add(userOptional.get());
        } else if (request.getParam() != null) {
            List<User> users = userRepository.getAllBy(request.getParam());
            response.addAll(users);
        }

        logger.info("***** REQUEST OK *****");
        return ResponseEntity.status(HttpStatus.OK).body(response);
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

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordRequest request)
            throws ResourceNotFoundException, InvalidRequestException {
        logger.info("***** CHANGE PASSWORD *****");
        User loggedInUser = authenticationService.getUser();
        logger.info("Username: " + loggedInUser.getUsername());

        if(!loggedInUser.getPassword().equals(request.getCurrentPwd())){
            logger.warn("***** Incorrect current password *****");
            throw new InvalidRequestException("Incorrect current password");
        }

        loggedInUser.setPassword(passwordEncoder.encode(request.getNewPwd()));
        userRepository.save(loggedInUser);
        logger.info("***** PASSWORD CHANGED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password changed");
    }

}
