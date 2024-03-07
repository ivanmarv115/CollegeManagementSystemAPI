package ivanmartinez.simpleStudentsAPI.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateStudentRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String course;
    private String degree;
    private String dateOfBirth;
}
