package ivanmartinez.simpleStudentsAPI.DTO.Students;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GetStudentsResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer semester;
    private Set<Course> currentCourses;
    private Set<Course> passedCourses;
    private String degree;
    private String dateOfBirth;
    private String username;
}
