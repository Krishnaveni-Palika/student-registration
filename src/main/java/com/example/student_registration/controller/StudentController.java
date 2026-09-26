package com.example.student_registration.controller;

import com.example.student_registration.entity.Student;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.student_registration.service.StudentService;

@Tag(name = "Student API", description = "APIs for student registration")
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/createStudent")
    public Student createStudent(@Valid @RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/getStudentById/{id}")
    public Student getStudentById(@PathVariable Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than 0");
        }
        return studentService.getStudentById(id);
    }

    @PutMapping("/updateStudent/{id}")
    public Student updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than 0"
            );
        }
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@Positive @PathVariable Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than 0"
            );
        }
        studentService.deleteStudent(id);
    }
}
