package ivanmartinez.simpleStudentsAPI.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCourseRequest {
    private String name;
    private String code;
    private String year;
    private String degree;
}
