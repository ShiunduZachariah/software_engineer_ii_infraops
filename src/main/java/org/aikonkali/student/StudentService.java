package org.aikonkali.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
        if (existingStudent.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        return studentRepository.save(student);
    }
}

