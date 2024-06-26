package ivanmartinez.simpleStudentsAPI.DTO.Courses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrolledStudent {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer semester;
    private String degree;
}
