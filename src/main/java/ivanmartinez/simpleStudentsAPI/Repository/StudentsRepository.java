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
        SELECT s FROM Student s LEFT JOIN s.degree d 
        WHERE LOWER(s.firstName) LIKE %:param% 
        OR LOWER(s.lastName) LIKE %:param% 
        OR LOWER(s.semester) LIKE %:param% 
        OR LOWER(s.dateOfBirth) LIKE %:param% 
        OR LOWER(d.name) LIKE %:param%"
        """
    )
    List<Student> getAllBy(@Param("param") String param);
}
