package ivanmartinez.simpleStudentsAPI.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class CustomException extends Exception{
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timeStamp;

    public CustomException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }
}
