package ivanmartinez.simpleStudentsAPI.Exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomException{
    public ResourceNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
