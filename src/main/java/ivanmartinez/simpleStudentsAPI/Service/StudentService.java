package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {

    ResponseEntity<String> createStudent(Student student);

    List<Student> getAllStudents();

    ResponseEntity<String> deleteStudent(Long id);

    ResponseEntity<String> updateStudent(Student student);
}
