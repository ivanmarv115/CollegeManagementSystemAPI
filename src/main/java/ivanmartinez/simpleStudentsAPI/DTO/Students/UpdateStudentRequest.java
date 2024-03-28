package ivanmartinez.simpleStudentsAPI.DTO.Students;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStudentRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer semester;
    private String dateOfBirth;
}
