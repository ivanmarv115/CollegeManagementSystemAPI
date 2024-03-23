package ivanmartinez.simpleStudentsAPI.DTO.Courses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCourseRequest {
    private Long id;
    private String name;
    private String code;
    private Integer semester;
    private String degree;
}
