package ivanmartinez.simpleStudentsAPI.DTO;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GetProfessorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<Course> coursesTaught;
    private String username;
}
