package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {

    ResponseEntity<String> createStudent(Student student);

    List<Student> getAllStudents();

    void deleteStudent(Long id);

    void updateStudent(Student student);
}
