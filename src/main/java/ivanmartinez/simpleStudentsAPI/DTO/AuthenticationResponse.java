package ivanmartinez.simpleStudentsAPI.DTO;

import ivanmartinez.simpleStudentsAPI.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Role role;
    private String token;
    private String message;

}
