package ivanmartinez.simpleStudentsAPI.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private Integer semester;

    @ManyToMany(mappedBy = "requiredCourses")
    @JsonIgnore
    private Set<Degree> requiredForDegree;

    @ManyToMany(mappedBy = "optionalCourses")
    @JsonIgnore
    private Set<Degree> optionalForDegree;

    @ManyToMany(mappedBy = "currentCourses")
    @JsonIgnore
    private Set<Student> students;

    @ManyToMany(mappedBy = "coursesTaught")
    @JsonIgnore
    private Set<Professor> professors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    @JsonIgnore
    private Set<Course> coursesPrerequisites;

}
