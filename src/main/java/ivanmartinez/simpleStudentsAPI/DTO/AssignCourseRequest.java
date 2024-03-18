package ivanmartinez.simpleStudentsAPI.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignCourseRequest {
    private Long professorId;
    private Long courseId;
}
