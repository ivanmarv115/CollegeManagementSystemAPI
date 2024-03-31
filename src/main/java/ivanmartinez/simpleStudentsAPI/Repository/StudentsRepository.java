package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {
    @Query("""
         SELECT s FROM Student s
         LEFT JOIN s.degree d
         WHERE LOWER(s.firstName) LIKE CONCAT('%', LOWER(:param), '%')
         OR LOWER(s.lastName) LIKE CONCAT('%', LOWER(:param), '%')
         OR LOWER(s.dateOfBirth) LIKE CONCAT('%', LOWER(:param), '%')
         OR CAST(s.semester AS string) LIKE CONCAT('%', LOWER(:param), '%')
         OR LOWER(d.name) LIKE CONCAT('%', LOWER(:param), '%')
        """
    )
    List<Student> getAllBy(@Param("param") String param);
}
