package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {
    List<Student> findByFirstNameContainingAndLastNameContainingAndCourseContaining(
            String firstName,
            String lastName,
            String course
    );
}
