package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CoursesController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Long> createCourse(
            @RequestBody CreateCourseRequest request
            ) throws CustomException {
        return courseService.createCourse(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Course>> getAllCourses() throws CustomException {
        return courseService.getAllCourses();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> updateCourse(@RequestBody UpdateCourseRequest request)
            throws CustomException {
        return courseService.updateCourse(request);
    }
}
