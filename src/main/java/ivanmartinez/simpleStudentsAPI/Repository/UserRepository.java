package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);

    @Query("""
         SELECT u FROM User u
         WHERE LOWER(u.username) LIKE CONCAT('%', LOWER(:param), '%')
         OR LOWER(u.role) LIKE CONCAT('%', LOWER(:param), '%')
        """
    )
    List<User> getAllBy(@Param("param") String param);

}
