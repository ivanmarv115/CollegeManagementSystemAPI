package ivanmartinez.simpleStudentsAPI.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String year;
    private String degree;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    @ToString.Exclude
    private Set<Student> students;

}
