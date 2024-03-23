package ivanmartinez.simpleStudentsAPI.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCoursePrerequisiteRequest {
    private Long courseId;
    private Long prerequisiteCourseId;
}
