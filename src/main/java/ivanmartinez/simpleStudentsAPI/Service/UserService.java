package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.ChangePasswordRequest;
import ivanmartinez.simpleStudentsAPI.DTO.CreateUserRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User createUser(CreateUserRequest createUserRequest) throws ResourceAlreadyExistsException;

    ResponseEntity<String> lockUser(LongIdRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> unlockUser(LongIdRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> addUser(CreateUserRequest request) throws ResourceAlreadyExistsException;

    ResponseEntity<String> changePassword(ChangePasswordRequest request)
            throws ResourceNotFoundException, InvalidRequestException;
}
