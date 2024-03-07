package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseEntity<Long> createCourse(Course course) throws CustomException;
}
