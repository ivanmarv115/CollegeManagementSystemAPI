package ivanmartinez.simpleStudentsAPI.DTO;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GetStudentsResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String course;
    private Set<Course> courses;
    private String degree;
    private String dateOfBirth;
    private String username;
}
