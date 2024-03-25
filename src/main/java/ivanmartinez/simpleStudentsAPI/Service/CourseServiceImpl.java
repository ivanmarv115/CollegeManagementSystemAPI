package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.CourseIdPrerequisiteIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.GetCourseResponse;
import ivanmartinez.simpleStudentsAPI.DTO.LongIdRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.UpdateCourseRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Override
    public ResponseEntity<Long> createCourse(CreateCourseRequest request) throws ResourceAlreadyExistsException {
        logger.info("***** CREATE COURSE *****");
        logger.info("New course: " + request);

        if(courseRepository.existsByCode(request.getCode())){
            logger.warn("Course with code " + request.getCode() + " already exists");
            throw new ResourceAlreadyExistsException("Course with code " +
                    request.getCode() + " already exists");
        }

        Course course = createCourseRequestToCourseEntity(request);
        logger.info("COURSE CREATED");
        return new ResponseEntity<>(courseRepository.save(course).getId(), HttpStatus.CREATED);
    }

    private Course createCourseRequestToCourseEntity(CreateCourseRequest request){
        return Course.builder()
                .name(request.getName())
                .code(request.getCode())
                .semester(request.getSemester())
                .students(new HashSet<>())
                .coursesPrerequisites(new HashSet<>())
                .build();
    }

    @Override
    public ResponseEntity<List<Course>> getAllCourses(){
        logger.info("***** GET ALL COURSES *****");
        List<Course> courses = courseRepository.findAll();

        List<GetCourseResponse> response = new ArrayList<>();
        for(Course course : courses){
            response.add(
                    GetCourseResponse.builder()
                            .id(course.getId())
                            .code(course.getCode())
                            .build()
            );
        }

        logger.info("***** SUCCESS *****");
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteCourse(LongIdRequest longIdRequest) throws ResourceNotFoundException {
        logger.info("***** DELETE COURSE *****");
        logger.info("id: " + longIdRequest.getLongId());
        if(!courseRepository.existsById(longIdRequest.getLongId())){
            logger.warn("Course does not exists");
            throw new ResourceNotFoundException("Course does not exists");
        }
        courseRepository.deleteById(longIdRequest.getLongId());
        logger.info("***** DELETED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Course Deleted");
    }

    @Override
    public ResponseEntity<String> updateCourse(UpdateCourseRequest requestCourse) throws ResourceNotFoundException {
        logger.info("***** UPDATE COURSE *****");
        logger.info("New Course: " + requestCourse);
        if(!courseRepository.existsById(requestCourse.getId())){
            logger.warn("Course does not exists");
            throw new ResourceNotFoundException("Course does not exists");
        }
        Course courseToUpdate = courseRepository.findById(requestCourse.getId()).get();
        logger.info("Course to udpate: " + courseToUpdate);

        courseToUpdate.setName(requestCourse.getName());
        courseToUpdate.setCode(requestCourse.getCode());
        courseToUpdate.setSemester(requestCourse.getSemester());
        courseRepository.save(courseToUpdate);
        logger.info("***** UPDATED *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Course updated");
    }

    @Override
    public ResponseEntity<String> addCoursePrerequisite(CourseIdPrerequisiteIdRequest request) throws ResourceNotFoundException {
        logger.info("***** ADD PREREQUISITE COURSE *****");
        logger.info("Request: " + request);
        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (courseOptional.isEmpty()){
            logger.warn("Course not found");
            throw new ResourceNotFoundException("Course not found");
        }
        Course course = courseOptional.get();

        Optional<Course> coursePrerequisiteOptional = courseRepository.findById(request.getPrerequisiteCourseId());
        if (coursePrerequisiteOptional.isEmpty()){
            logger.warn("Prerequisite course not found");
            throw new ResourceNotFoundException("Prerequisite course not found");
        }
        logger.info("Resources found");

        course.getCoursesPrerequisites().add(coursePrerequisiteOptional.get());
        courseRepository.save(course);
        logger.info("***** SUCCESS *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully added prerequisite");
    }

    @Override
    public ResponseEntity<String> removeCoursePrerequisite(CourseIdPrerequisiteIdRequest request) throws ResourceNotFoundException {
        logger.info("***** REMOVE PREREQUISITE COURSE *****");
        logger.info("Request: " + request);
        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (courseOptional.isEmpty()){
            logger.warn("Course not found");
            throw new ResourceNotFoundException("Course not found");
        }
        Course course = courseOptional.get();

        Optional<Course> coursePrerequisiteOptional = courseRepository.findById(request.getPrerequisiteCourseId());
        if (coursePrerequisiteOptional.isEmpty()){
            logger.warn("Prerequisite course not found");
            throw new ResourceNotFoundException("Prerequisite course not found");
        }
        logger.info("Resources found");

        course.getCoursesPrerequisites().remove(coursePrerequisiteOptional.get());
        courseRepository.save(course);
        logger.info("***** REMOVED SUCCESSFULLY *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully removed prerequisite");
    }


}
