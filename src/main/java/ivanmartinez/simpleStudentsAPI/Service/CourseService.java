package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {
    ResponseEntity<Long> createCourse(CreateCourseRequest request) throws CustomException;

    ResponseEntity<List<Course>> getAllCourses() throws CustomException;

    ResponseEntity<String> deleteCourse(LongIdRequest longIdRequest) throws CustomException;

    ResponseEntity<String> updateCourse(UpdateCourseRequest requestCourse) throws CustomException;
}
