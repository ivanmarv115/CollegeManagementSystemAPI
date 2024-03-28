package ivanmartinez.simpleStudentsAPI.DTO.Courses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetCourseResponse {
    private Long id;
    private String name;
    private String code;
    private Integer semester;
    private List<String> requiredForDegree;
    private List<String> optionalForDegree;
    private List<EnrolledStudent> students;
    private List<AssignedProfessor> professors;
}
