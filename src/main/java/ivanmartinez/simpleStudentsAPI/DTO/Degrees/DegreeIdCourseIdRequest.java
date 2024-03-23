package ivanmartinez.simpleStudentsAPI.DTO.Degrees;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DegreeIdCourseIdRequest {
    private Long degreeId;
    private Long courseId;
}
