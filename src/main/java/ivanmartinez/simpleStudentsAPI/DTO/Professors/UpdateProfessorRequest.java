package ivanmartinez.simpleStudentsAPI.DTO.Professors;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateProfessorRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
}
