package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {
    Optional<Degree> findByName(String name);

    @Query("""
         SELECT d FROM Degree d
         WHERE LOWER(d.name) LIKE CONCAT('%', LOWER(:param), '%')
        """
    )
    List<Degree> getAllBy(@Param("param") String param);
}
