package ivanmartinez.simpleStudentsAPI.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LongIdRequest {
    private Long longId;
    private String msg;
}
