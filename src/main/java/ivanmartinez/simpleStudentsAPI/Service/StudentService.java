package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Students.*;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    ResponseEntity<String> createStudent(CreateStudentRequest createStudentRequest) throws ResourceAlreadyExistsException;

    ResponseEntity<List<GetStudentsResponse>> getAllStudents();

    ResponseEntity<List<GetStudentsResponse>> getStudentsContaining(GetByRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> deleteStudent(LongIdRequest id) throws ResourceNotFoundException;

    ResponseEntity<String> updateStudent(UpdateStudentRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> enrollToCourse(StudentIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException;

    ResponseEntity<String> addPassedCourse(StudentIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException;

    ResponseEntity<String> enrollToDegree(StudentIdDegreeIdRequest request) throws ResourceNotFoundException;

    ResponseEntity<String> unrollToCourse(StudentIdCourseIdRequest request) throws ResourceNotFoundException, InvalidRequestException;
}
