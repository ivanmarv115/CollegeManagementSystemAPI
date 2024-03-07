package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Config.JwtService;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User createUser(CreateUserRequest createUserRequest) throws CustomException {
        Optional<User> userByUsername = userRepository.findByUsername(
                createUserRequest.getUsername()
        );
        if (userByUsername.isPresent()){
            logger.warn("Username already exists");
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
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
}
