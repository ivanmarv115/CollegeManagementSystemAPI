package ivanmartinez.simpleStudentsAPI.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex){
        ErrorBody errorBody = ErrorBody.builder()
                .status(ex.getStatus())
                .message(ex.getMessage())
                .timeStamp(ex.getTimeStamp())
                .build();
        return new ResponseEntity<>(errorBody, errorBody.getStatus());
    }
}
