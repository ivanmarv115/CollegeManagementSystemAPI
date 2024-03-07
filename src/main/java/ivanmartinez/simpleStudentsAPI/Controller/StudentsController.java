package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.CreateStudentRequest;
import ivanmartinez.simpleStudentsAPI.DTO.GetStudentsResponse;
import ivanmartinez.simpleStudentsAPI.DTO.IdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin
public class StudentsController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<GetStudentsResponse>> getAllStudents(){
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
            @RequestBody IdRequest idRequest
            ) throws CustomException {
        return studentService.deleteStudent(idRequest);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    public ResponseEntity<String> updateStudent(
            @RequestBody Student student
    ) throws CustomException {
        return studentService.updateStudent(student);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
