package ivanmartinez.simpleStudentsAPI.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "degree_required_courses_mapping", joinColumns = @JoinColumn(name = "degree_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> requiredCourses;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "degree_optional_courses_mapping", joinColumns = @JoinColumn(name = "degree_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> optionalCourses;
}
