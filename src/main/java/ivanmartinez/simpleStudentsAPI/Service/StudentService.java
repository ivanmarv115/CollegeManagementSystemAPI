package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Entity.Student;

import java.util.List;

public interface StudentService {

    void createStudent(Student student);

    List<Student> getAllStudents();

    void deleteStudent(Long id);

    void updateStudent(Student student);
}
