package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(){
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createStudent(
            @RequestBody @Valid Student student
    ){
        return studentService.createStudent(student);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteStudent(
            @RequestBody Map<String, String> payload
            ){
        return studentService.deleteStudent(Long.valueOf(payload.get("id")));
    }

    @PutMapping
    public ResponseEntity<String> updateStudent(
            @RequestBody Student student
    ){
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
