package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody Student student
    ){

        return studentService.createStudent(student);

    }

    @DeleteMapping
    public ResponseEntity<String> deleteStudent(
            @RequestBody Map<String, String> payload
            ){
        studentService.deleteStudent(Long.valueOf(payload.get("id")));
        return new ResponseEntity<String>("Student deleted", HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<String> updateStudent(
            @RequestBody Student student
    ){
        studentService.updateStudent(student);
        return new ResponseEntity<String>("Student updated", HttpStatus.ACCEPTED);
    }
}
