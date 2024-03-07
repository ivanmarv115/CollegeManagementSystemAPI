package ivanmartinez.simpleStudentsAPI.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends Exception{
    private HttpStatus status;
    private String message;
    private LocalDateTime timeStamp;

    public CustomException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }
}
