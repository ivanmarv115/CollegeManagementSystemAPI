package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.StudentCourseEnrollRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    ResponseEntity<String> createStudent(CreateStudentRequest createStudentRequest) throws CustomException;

    ResponseEntity<List<GetStudentsResponse>> getAllStudents() throws CustomException;

    ResponseEntity<String> deleteStudent(LongIdRequest id) throws CustomException;

    ResponseEntity<String> updateStudent(Student student) throws CustomException;

    ResponseEntity<List<Student>> getFilteredStudents(Optional<String> firstName,
                                                      Optional<String> lastName,
                                                      Optional<String> course);

    ResponseEntity<String> enrollToCourse(StudentCourseEnrollRequest request) throws CustomException;
}
