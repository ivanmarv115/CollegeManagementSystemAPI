package ivanmartinez.simpleStudentsAPI.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorBody {
    private String message;
    private HttpStatus status;
    private LocalDateTime timeStamp;
}
