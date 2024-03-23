package ivanmartinez.simpleStudentsAPI.DTO.Professors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignCourseRequest {
    private Long professorId;
    private Long courseId;
}
