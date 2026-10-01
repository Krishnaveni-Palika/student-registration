package com.example.student_registration.controller;

import com.example.student_registration.DTO.StudentRequestDTO;
import com.example.student_registration.DTO.StudentResponseDTO;
import com.example.student_registration.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Student API",
        description = "APIs for student registration"
)
@RestController
@RequestMapping("/student-registration")
@Validated
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE STUDENT
    @PostMapping("/student")
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO request) {

        StudentResponseDTO response =
                studentService.createStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET STUDENT BY ID
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById(
            @PathVariable
            @Positive(message = "Student ID must be greater than 0")
            Integer studentId) {

        StudentResponseDTO response =
                studentService.getStudentById(studentId);

        return ResponseEntity.ok(response);
    }

    // UPDATE STUDENT
    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable
            @Positive(message = "Student ID must be greater than 0")
            Integer studentId,

            @Valid @RequestBody StudentRequestDTO request) {

        StudentResponseDTO response =
                studentService.updateStudent(studentId, request);

        return ResponseEntity.ok(response);
    }

    // DELETE STUDENT
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable
            @Positive(message = "Student ID must be greater than 0")
            Integer studentId) {

        studentService.deleteStudent(studentId);

        return ResponseEntity.noContent().build();
    }
}