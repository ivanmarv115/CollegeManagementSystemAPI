package ivanmartinez.simpleStudentsAPI.DTO.Courses;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GetCourseResponse {
    private Long id;
    private String name;
    private String code;
    private String year;
    private String degree;
    private Set<EnrolledStudent> students;
    private Set<AssignedProfessor> professors;
}
