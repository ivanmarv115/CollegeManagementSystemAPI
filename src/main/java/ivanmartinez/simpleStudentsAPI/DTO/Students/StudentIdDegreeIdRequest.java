package ivanmartinez.simpleStudentsAPI.DTO.Students;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentIdDegreeIdRequest {
    private Long studentId;
    private Long degreeId;
}
