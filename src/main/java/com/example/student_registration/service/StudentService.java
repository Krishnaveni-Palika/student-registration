package com.example.student_registration.service;

import com.example.student_registration.entity.Student;
import com.example.student_registration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.student_registration.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {

        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with given id: " + id));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student updateStudent(Long id, Student studentInfo) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with given id: " + id));
        student.setName(studentInfo.getName());
        student.setGender(studentInfo.getGender());
        student.setDateOfBirth(studentInfo.getDateOfBirth());
        return studentRepository.save(student);

    }

    public void deleteStudent(Long id) {
        try {
            studentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }
}
