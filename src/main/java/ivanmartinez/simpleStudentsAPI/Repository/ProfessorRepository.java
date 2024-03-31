package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    @Query("""
         SELECT p FROM Professor p
         WHERE LOWER(p.firstName) LIKE CONCAT('%', LOWER(:param), '%')
         OR LOWER(p.lastName) LIKE CONCAT('%', LOWER(:param), '%')
        """
    )
    List<Professor> getAllBy(@Param("param") String param);
}
