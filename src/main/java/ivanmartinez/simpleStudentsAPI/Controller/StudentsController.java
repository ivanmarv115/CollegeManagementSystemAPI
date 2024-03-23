package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.Students.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Students.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Students.StudentIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
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
    public ResponseEntity<List<GetStudentsResponse>> getAllStudents() throws CustomException {
        return studentService.getAllStudents();
    }

    @GetMapping("/by")
    public ResponseEntity<List<Student>> getFilteredStudents(@RequestParam(required = false) Optional<String> firstName,
                                                             @RequestParam(required = false) Optional<String> lastName,
                                                             @RequestParam(required = false) Optional<String> course){


        return studentService.getFilteredStudents(firstName, lastName, course);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<String> createStudent(
            @RequestBody CreateStudentRequest createStudentRequest
    ) throws CustomException {
        return studentService.createStudent(createStudentRequest);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping
    public ResponseEntity<String> deleteStudent(
            @RequestBody LongIdRequest longIdRequest
            ) throws CustomException {
        return studentService.deleteStudent(longIdRequest);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    public ResponseEntity<String> updateStudent(
            @RequestBody Student student
    ) throws CustomException {
        return studentService.updateStudent(student);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/enroll")
    public ResponseEntity<String> enrollStudent(
            @RequestBody StudentIdCourseIdRequest request) throws CustomException {
        return studentService.enrollToCourse(request);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/addPassed")
    public ResponseEntity<String> addPassedCourse(
            @RequestBody StudentIdCourseIdRequest request) throws CustomException {
        return studentService.addPassedCourse(request);
    }

}
