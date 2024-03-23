package ivanmartinez.simpleStudentsAPI.DTO.Degrees;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDegreeRequest {
    private String name;
}
