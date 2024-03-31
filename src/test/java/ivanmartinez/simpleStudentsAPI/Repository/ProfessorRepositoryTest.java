package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Professor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProfessorRepositoryTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Test
    public void testGetAllBy() {
        // Create test data
        Professor professor1 = new Professor();
        professor1.setFirstName("John");
        professor1.setLastName("Doe");

        Professor professor2 = new Professor();
        professor2.setFirstName("Jane");
        professor2.setLastName("Smith");

        professorRepository.save(professor1);
        professorRepository.save(professor2);

        // Test case 1: Search by first name
        List<Professor> result1 = professorRepository.getAllBy("john");
        assertEquals(1, result1.size());
        assertEquals("John", result1.get(0).getFirstName());

        // Test case 2: Search by last name
        List<Professor> result2 = professorRepository.getAllBy("smith");
        assertEquals(1, result2.size());
        assertEquals("Jane", result2.get(0).getFirstName());

        // Test case 3: Search by partial name
        List<Professor> result3 = professorRepository.getAllBy("n");
        assertEquals(2, result3.size());

        // Test case 4: Search by non-existing name
        List<Professor> result4 = professorRepository.getAllBy("xyz");
        assertEquals(0, result4.size());
    }
}