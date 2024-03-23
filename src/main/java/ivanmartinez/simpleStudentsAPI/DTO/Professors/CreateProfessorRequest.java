package ivanmartinez.simpleStudentsAPI.DTO.Professors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProfessorRequest {
    private String firstName;
    private String lastName;
    private String username;
}
