package ivanmartinez.simpleStudentsAPI.Controller;

import ivanmartinez.simpleStudentsAPI.DTO.AddCoursePrerequisiteRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Long> createCourse(
            @RequestBody CreateCourseRequest request
            ) throws CustomException {
        return courseService.createCourse(request);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Course>> getAllCourses() throws CustomException {
        return courseService.getAllCourses();
    }

    @PutMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> updateCourse(@RequestBody UpdateCourseRequest request)
            throws CustomException {
        return courseService.updateCourse(request);
    }

    @PatchMapping("/addPrerequisite")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addPrerequisite(@RequestBody AddCoursePrerequisiteRequest request)
            throws ResourceNotFoundException {
        return courseService.addCoursePrerequisite(request);
    }
}
