package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class DegreeRepositoryTest {

    @Autowired
    private DegreeRepository degreeRepository;

    @Test
    public void testGetAllBy() {
        // Create test data
        Degree degree1 = new Degree();
        degree1.setName("Computer Science");

        Degree degree2 = new Degree();
        degree2.setName("Electrical Engineering");

        degreeRepository.save(degree1);
        degreeRepository.save(degree2);

        // Test case 1: Search by full name
        List<Degree> result1 = degreeRepository.getAllBy("Computer Science");
        assertEquals(1, result1.size());
        assertEquals("Computer Science", result1.get(0).getName());

        // Test case 2: Search by partial name
        List<Degree> result2 = degreeRepository.getAllBy("engineering");
        assertEquals(1, result2.size());
        assertEquals("Electrical Engineering", result2.get(0).getName());

        // Test case 3: Search by case-insensitive name
        List<Degree> result3 = degreeRepository.getAllBy("computer science");
        assertEquals(1, result3.size());
        assertEquals("Computer Science", result3.get(0).getName());

        // Test case 4: Search by non-existing name
        List<Degree> result4 = degreeRepository.getAllBy("Mathematics");
        assertEquals(0, result4.size());
    }
}