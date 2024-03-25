package ivanmartinez.simpleStudentsAPI.DTO.Degrees;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateDegreeRequest {
    private Long degreeId;
    private String name;
}
