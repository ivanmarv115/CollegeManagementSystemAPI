package ivanmartinez.simpleStudentsAPI.Repository;

import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentsRepositoryTest {

    @Autowired
    private StudentsRepository studentsRepository;

    @Test
    void shouldGetAllBy() {
        // Given
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setSemester(1);
        student1.setDateOfBirth("1990-01-01");

        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setSemester(2);
        student2.setDateOfBirth("1991-02-02");

        Degree degree = new Degree();
        degree.setName("Computer Science");
        student1.setDegree(degree);
        student2.setDegree(degree);

        studentsRepository.save(student1);
        studentsRepository.save(student2);

        // When
        List<Student> students = studentsRepository.getAllBy("Joh");

        // Test
        assertEquals(1, students.size());
        assertEquals("John", students.get(0).getFirstName());

        students = studentsRepository.getAllBy("Smi");
        assertEquals(1, students.size());
        assertEquals("Jane", students.get(0).getFirstName());

        students = studentsRepository.getAllBy("1");
        assertEquals(2, students.size());

        students = studentsRepository.getAllBy("Computer");
        assertEquals(2, students.size());

        students = studentsRepository.getAllBy("NonExistingValue");
        assertEquals(0, students.size());
    }
}