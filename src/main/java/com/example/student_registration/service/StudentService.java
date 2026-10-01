package com.example.student_registration.service;

import com.example.student_registration.DTO.StudentRequestDTO;
import com.example.student_registration.DTO.StudentResponseDTO;
import com.example.student_registration.exception.BadRequestException;
import com.example.student_registration.exception.ResourceNotFoundException;
import com.example.student_registration.entity.Student;
import com.example.student_registration.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CREATE STUDENT
    public StudentResponseDTO createStudent(StudentRequestDTO request) {

        if (request == null) {
            throw new BadRequestException(
                    "Student request is required"
            );
        }

        // Student ID must be unique
        if (studentRepository.existsById(request.getStudentId())) {
            throw new BadRequestException(
                    "Student ID " + request.getStudentId()
                            + " already exists"
            );
        }

        Student student = new Student();

        student.setStudentId(request.getStudentId());
        student.setName(request.getName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());

        Student savedStudent =
                studentRepository.save(student);

        return convertToResponseDTO(savedStudent);
    }


    // GET STUDENT BY ID
    public StudentResponseDTO getStudentById(Integer studentId) {

        if (studentId == null) {
            throw new BadRequestException(
                    "Student ID is required"
            );
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with ID "
                                        + studentId
                        )
                );

        return convertToResponseDTO(student);
    }


    // UPDATE STUDENT
    public StudentResponseDTO updateStudent(
            Integer studentId,
            StudentRequestDTO request) {

        if (studentId == null) {
            throw new BadRequestException(
                    "Student ID is required"
            );
        }

        if (request == null) {
            throw new BadRequestException(
                    "Student request is required"
            );
        }

        Student existingStudent =
                studentRepository.findById(studentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with ID "
                                                + studentId
                                )
                        );

        // Student ID cannot be changed
        if (!studentId.equals(request.getStudentId())) {
            throw new BadRequestException(
                    "Student ID cannot be changed"
            );
        }

        // Update fields
        existingStudent.setName(request.getName());
        existingStudent.setDateOfBirth(
                request.getDateOfBirth()
        );
        existingStudent.setGender(request.getGender());

        Student updatedStudent =
                studentRepository.save(existingStudent);

        return convertToResponseDTO(updatedStudent);
    }


    // DELETE STUDENT
    public void deleteStudent(Integer studentId) {

        if (studentId == null) {
            throw new BadRequestException(
                    "Student ID is required"
            );
        }

        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException(
                    "Student not found with ID " + studentId
            );
        }

        studentRepository.deleteById(studentId);
    }


    // ENTITY TO RESPONSE DTO
    private StudentResponseDTO convertToResponseDTO(
            Student student) {

        return new StudentResponseDTO(
                student.getStudentId(),
                student.getName(),
                student.getDateOfBirth(),
                student.getGender()
        );
    }
}