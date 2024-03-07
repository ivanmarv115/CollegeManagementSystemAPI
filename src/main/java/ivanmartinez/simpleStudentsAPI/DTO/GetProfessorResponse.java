package ivanmartinez.simpleStudentsAPI.DTO;

import ivanmartinez.simpleStudentsAPI.Entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetProfessorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private List<String> coursesTaught;
    private String username;
}
