package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addUser(@RequestBody CreateUserRequest request)
            throws ResourceAlreadyExistsException {
        return userService.addUser(request);
    }

    @PatchMapping("/lockUser")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> lockUser(@RequestBody LongIdRequest request)
            throws ResourceNotFoundException {
        return userService.lockUser(request);
    }

    @PatchMapping("/unlockUser")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> unlockUser(@RequestBody LongIdRequest request)
            throws ResourceNotFoundException {
        return userService.unlockUser(request);
    }
}
