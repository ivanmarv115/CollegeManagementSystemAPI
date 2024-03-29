package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.GetByRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Students.*;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.InvalidRequestException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<GetStudentsResponse>> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/by")
    public ResponseEntity<List<GetStudentsResponse>> getFilteredStudents(
            @RequestBody GetByRequest request
            ){
        return studentService.getStudentsContaining(request);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<String> createStudent(
            @RequestBody CreateStudentRequest createStudentRequest
    ) throws ResourceAlreadyExistsException {
        return studentService.createStudent(createStudentRequest);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping
    public ResponseEntity<String> deleteStudent(
            @RequestBody LongIdRequest longIdRequest
            ) throws ResourceNotFoundException {
        return studentService.deleteStudent(longIdRequest);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    public ResponseEntity<String> updateStudent(
            @RequestBody UpdateStudentRequest request
    ) throws ResourceNotFoundException {
        return studentService.updateStudent(request);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/enrollToCourse")
    public ResponseEntity<String> enrollToCourse(
            @RequestBody StudentIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        return studentService.enrollToCourse(request);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/unrollToCourse")
    public ResponseEntity<String> unrollToCourse(
            @RequestBody StudentIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        return studentService.unrollToCourse(request);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/addPassed")
    public ResponseEntity<String> addPassedCourse(
            @RequestBody StudentIdCourseIdRequest request)
            throws InvalidRequestException, ResourceNotFoundException {
        return studentService.addPassedCourse(request);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/enrollToDegree")
    public ResponseEntity<String> enrollToDegree(
            @RequestBody StudentIdDegreeIdRequest request)
            throws ResourceNotFoundException {
        return studentService.enrollToDegree(request);
    }

}
