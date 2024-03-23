package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.AddCoursePrerequisiteRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Courses.CreateCourseRequest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;

    Logger logger = LoggerFactory.getLogger(CourseService.class);
    @Override
    public ResponseEntity<Long> createCourse(CreateCourseRequest request) throws CustomException {
        try {
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
        }catch (RuntimeException ex){
            logger.error(ex.getMessage());
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<List<Course>> getAllCourses() throws CustomException {
        try {
            logger.info("***** GET ALL COURSES *****");
            List<Course> courses = courseRepository.findAll();
            logger.info("SUCCESS");
            return new ResponseEntity<>(courses, HttpStatus.OK);
        }catch (RuntimeException ex){
            logger.error(ex.getMessage());
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteCourse(LongIdRequest longIdRequest) throws CustomException {

        try{
            logger.info("***** DELETE COURSE *****");
            logger.info("id: " + longIdRequest.getLongId());
            if(!courseRepository.existsById(longIdRequest.getLongId())){
                logger.warn("Course does not exists");
                throw new ResourceNotFoundException("Course does not exists");
            }
            courseRepository.deleteById(longIdRequest.getLongId());
            logger.info("DELETED");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Course Deleted");
        }catch (RuntimeException exception){
            logger.error(exception.getMessage());
            throw new CustomException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateCourse(UpdateCourseRequest requestCourse) throws CustomException {
        try{
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
            logger.info("UPDATED");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Course updated");
        }catch (RuntimeException exception){
            throw new CustomException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addCoursePrerequisite(AddCoursePrerequisiteRequest request) throws ResourceNotFoundException {
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


}
