package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);

    @Query("""
         SELECT c FROM Course c
         WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:param), '%')
         OR LOWER(c.code) LIKE CONCAT('%', LOWER(:param), '%')
         OR CAST(c.semester AS string) LIKE CONCAT('%', LOWER(:param), '%')
        """
    )
    List<Course> getAllBy(@Param("param") String param);
}
