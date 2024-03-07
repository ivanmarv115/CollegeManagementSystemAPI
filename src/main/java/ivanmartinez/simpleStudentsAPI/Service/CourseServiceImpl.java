package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Exception.CustomException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;

    Logger logger = LoggerFactory.getLogger(CourseService.class);
    @Override
    public ResponseEntity<Long> createCourse(Course course) throws CustomException {
        try {
            logger.info("***** CREATE COURSE *****");
            logger.info("New course: " + course);

            if(courseRepository.existsByCode(course.getCode())){
                logger.warn("Course with code " + course.getCode() + " already exists");
                throw new CustomException("Course with code " + course.getCode() + " already exists",
                        HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(courseRepository.save(course).getId(), HttpStatus.CREATED);
        }catch (RuntimeException ex){
            logger.error(ex.getMessage());
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
