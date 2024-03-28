package ivanmartinez.simpleStudentsAPI.DTO.Courses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignedProfessor {
    private Long id;
    private String firstName;
    private String lastName;
}
