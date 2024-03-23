package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.DTO.Degrees.DegreeIdCourseIdRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Course;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceNotFoundException;
import ivanmartinez.simpleStudentsAPI.Repository.CourseRepository;
import ivanmartinez.simpleStudentsAPI.Repository.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService{

    private final DegreeRepository degreeRepository;
    private final CourseRepository courseRepository;
    private final Logger logger = LoggerFactory.getLogger(DegreeService.class);

    @Override
    public ResponseEntity<String> createDegree(CreateDegreeRequest request) throws ResourceAlreadyExistsException {
        logger.info("***** CREATE DEGREE *****");
        logger.info("Request: " + request);

        if(degreeRepository.findByName(request.getName()).isPresent()){
            logger.warn("***** DEGREE ALREADY EXISTS *****");
            throw new ResourceAlreadyExistsException("Degree already exists");
        }

        Degree degree = Degree.builder()
                .name(request.getName())
                .build();
        degreeRepository.save(degree);
        logger.info("***** CREATED SUCCESSFULLY *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Degree created successfully");
    }

    @Override
    public ResponseEntity<List<Degree>> getAllDegrees() {
        logger.info("***** GET ALL DEGREES *****");
        return ResponseEntity.status(HttpStatus.OK)
                .body(degreeRepository.findAll());
    }

    @Override
    public ResponseEntity<String> addRequiredCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException {
        logger.info("***** ADD REQUIRED COURSE *****");
        logger.info("Request: " + request);

        Optional<Degree> degreeOptional = degreeRepository.findById(request.getDegreeId());
        if (degreeOptional.isEmpty()){
            logger.warn("***** DEGREE NOT FOUND *****");
            throw new ResourceNotFoundException("Degree not found");
        }
        Degree degree = degreeOptional.get();

        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (courseOptional.isEmpty()){
            logger.warn("***** COURSE NOT FOUND *****");
            throw new ResourceNotFoundException("Course not found");
        }

        degree.getRequiredCourses().add(courseOptional.get());
        degreeRepository.save(degree);
        logger.info("***** ADDED SUCCESSFULLY *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Required course added successfully");

    }


    @Override
    public ResponseEntity<String> addOptionalCourse(DegreeIdCourseIdRequest request) throws ResourceNotFoundException {
        logger.info("***** ADD REQUIRED COURSE *****");
        logger.info("Request: " + request);

        Optional<Degree> degreeOptional = degreeRepository.findById(request.getDegreeId());
        if (degreeOptional.isEmpty()){
            logger.warn("***** DEGREE NOT FOUND *****");
            throw new ResourceNotFoundException("Degree not found");
        }
        Degree degree = degreeOptional.get();

        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (courseOptional.isEmpty()){
            logger.warn("***** COURSE NOT FOUND *****");
            throw new ResourceNotFoundException("Course not found");
        }

        degree.getOptionalCourses().add(courseOptional.get());
        degreeRepository.save(degree);
        logger.info("***** ADDED SUCCESSFULLY *****");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Required course added successfully");

    }
}
