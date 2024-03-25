package ivanmartinez.simpleStudentsAPI.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseIdPrerequisiteIdRequest {
    private Long courseId;
    private Long prerequisiteCourseId;
}
