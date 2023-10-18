package ivanmartinez.simpleStudentsAPI.Service;

import ivanmartinez.simpleStudentsAPI.Entity.Student;
import ivanmartinez.simpleStudentsAPI.Repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentsRepository studentsRepository;

    @Override
    public void createStudent(Student student) {
        studentsRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentsRepository.findAll();
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentsRepository.findById(id).get();
        studentsRepository.delete(student);
    }

    @Override
    public void updateStudent(Student student) {
        Student studentById = studentsRepository.findById(Long.valueOf(student.getId())).get();
        studentById.setFirstName(student.getFirstName());
        studentById.setLastName(student.getLastName());
        studentById.setCourse(student.getCourse());
        studentById.setDateOfBirth(student.getDateOfBirth());
        studentsRepository.save(studentById);
    }
}
