package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAllBy() {
        // Create test data
        User user1 = new User();
        user1.setUsername("john.doe");

        Role role1 = Role.STUDENT;
        user1.setRole(role1);

        User user2 = new User();
        user2.setUsername("jane.smith");

        Role role2 = Role.PROFESSOR;
        user2.setRole(role2);

        userRepository.save(user1);
        userRepository.save(user2);

        // Test case 1: Search by username
        List<User> result1 = userRepository.getAllBy("john");
        assertEquals(1, result1.size());
        assertEquals("john.doe", result1.get(0).getUsername());

        // Test case 2: Search by role name
        List<User> result2 = userRepository.getAllBy("professor");
        assertEquals(1, result2.size());
        assertEquals("jane.smith", result2.get(0).getUsername());

        // Test case 3: Search by partial username
        List<User> result3 = userRepository.getAllBy("doe");
        assertEquals(1, result3.size());
        assertEquals("john.doe", result3.get(0).getUsername());

        // Test case 4: Search by case-insensitive role name
        List<User> result4 = userRepository.getAllBy("student");
        assertEquals(1, result4.size());
        assertEquals("john.doe", result4.get(0).getUsername());

        // Test case 5: Search by non-existing value
        List<User> result5 = userRepository.getAllBy("admin");
        assertEquals(0, result5.size());
    }
}