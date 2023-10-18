package ivanmartinez.simpleStudentsAPI.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "First name can not be blank")
    private String firstName;
    @NotBlank(message = "Last name can not be blank")
    private String lastName;
    @NotBlank(message = "Course can not be blank")
    private String course;
    private String dateOfBirth;

}
