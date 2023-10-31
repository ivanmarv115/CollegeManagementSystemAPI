package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.StudentFilterDTO;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    ResponseEntity<String> createStudent(Student student);

    ResponseEntity<List<Student>> getAllStudents();

    ResponseEntity<String> deleteStudent(Long id);

    ResponseEntity<String> updateStudent(Student student);

    ResponseEntity<List<Student>> getFilteredStudents(Optional<String> firstName,
                                                      Optional<String> lastName,
                                                      Optional<String> course);
}
