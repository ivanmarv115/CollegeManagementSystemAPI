package ivanmartinez.simpleStudentsAPI.DTO.Students;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentIdCourseIdRequest {
    private Long studentId;
    private Long courseId;
}
