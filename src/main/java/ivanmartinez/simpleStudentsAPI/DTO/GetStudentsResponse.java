package ivanmartinez.simpleStudentsAPI.DTO;

import ivanmartinez.simpleStudentsAPI.Entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetStudentsResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String course;
    private String degree;
    private String dateOfBirth;
    private String username;
}
