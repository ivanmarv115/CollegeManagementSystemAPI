package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldGetAllBy() {
        // Create test data
        Course course1 = new Course();
        course1.setName("Mathematics");
        course1.setCode("MATH101");
        course1.setSemester(1);

        Course course2 = new Course();
        course2.setName("English Literature");
        course2.setCode("ENG202");
        course2.setSemester(2);

        courseRepository.save(course1);
        courseRepository.save(course2);

        // Test case 1: Search by course name
        List<Course> result1 = courseRepository.getAllBy("math");
        assertEquals(1, result1.size());
        assertEquals("Mathematics", result1.get(0).getName());

        // Test case 2: Search by course code
        List<Course> result2 = courseRepository.getAllBy("eng");
        assertEquals(1, result2.size());
        assertEquals("ENG202", result2.get(0).getCode());

        // Test case 3: Search by semester
        List<Course> result3 = courseRepository.getAllBy("1");
        assertEquals(1, result3.size());
        assertEquals(1, result3.get(0).getSemester());

        // Test case 4: Search by partial name
        List<Course> result4 = courseRepository.getAllBy("lit");
        assertEquals(1, result4.size());
        assertEquals("English Literature", result4.get(0).getName());

        // Test case 5: Search by non-existing value
        List<Course> result5 = courseRepository.getAllBy("xyz");
        assertEquals(0, result5.size());
    }
}