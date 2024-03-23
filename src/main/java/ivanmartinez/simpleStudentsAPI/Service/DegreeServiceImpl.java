package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.DTO.Degrees.CreateDegreeRequest;
import ivanmartinez.simpleStudentsAPI.Entity.Degree;
import ivanmartinez.simpleStudentsAPI.Exception.ResourceAlreadyExistsException;
import ivanmartinez.simpleStudentsAPI.Repository.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService{

    private final DegreeRepository degreeRepository;
    private final Logger logger = LoggerFactory.getLogger(DegreeService.class);

    @Override
    public ResponseEntity<String> createDegree(CreateDegreeRequest request) throws ResourceAlreadyExistsException {
        logger.info("***** CREATE DEGREE *****");
        logger.info("Request: " + request);

        if(degreeRepository.findByName(request.getName()).isPresent()){
            logger.warn("Degree already exists");
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


}
