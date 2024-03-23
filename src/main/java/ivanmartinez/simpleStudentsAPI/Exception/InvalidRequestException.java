package ivanmartinez.simpleStudentsAPI.Exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends CustomException{

    public InvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
