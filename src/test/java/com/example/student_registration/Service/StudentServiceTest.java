package com.example.student_registration.Service;

import com.example.student_registration.entity.Student;
import com.example.student_registration.repository.StudentRepository;
import com.example.student_registration.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;


    // 1. CREATE STUDENT
    @Test
    void testCreateStudent() {

        Student student = new Student();
        student.setName("John");
        student.setDateOfBirth(LocalDate.of(2016, 5, 10));
        student.setGender("Male");

        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.createStudent(student);

        assertEquals("John", result.getName());
        assertEquals("Male", result.getGender());

        verify(studentRepository).save(student);
    }


    // 2. GET BY ID - Student exists
    @Test
    void testGetStudentById() {

        Student student = new Student();
        student.setName("John");
        student.setDateOfBirth(LocalDate.of(2016, 5, 10));
        student.setGender("Male");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertEquals("John", result.getName());
        assertEquals("Male", result.getGender());

        verify(studentRepository).findById(1L);
    }


    // 3. GET BY ID - Student does not exist
    @Test
    void testGetStudentByIdWhenStudentDoesNotExist() {

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.getStudentById(1L)
        );

        assertEquals("Student not found with given id: 1", exception.getMessage());

        verify(studentRepository).findById(1L);
    }


    // 4. GET ALL
    @Test
    void testGetAllStudents() {

        Student student1 = new Student();
        student1.setName("John");

        Student student2 = new Student();
        student2.setName("David");

        when(studentRepository.findAll())
                .thenReturn(List.of(student1, student2));

        List<Student> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("David", result.get(1).getName());

        verify(studentRepository).findAll();
    }


    // 5. UPDATE
    @Test
    void testUpdateStudent() {

        Student existingStudent = new Student();
        existingStudent.setName("John");
        existingStudent.setDateOfBirth(LocalDate.of(2016, 5, 10));
        existingStudent.setGender("Male");

        Student studentDetails = new Student();
        studentDetails.setName("John Updated");
        studentDetails.setDateOfBirth(LocalDate.of(2016, 6, 15));
        studentDetails.setGender("Male");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(existingStudent))
                .thenReturn(existingStudent);

        Student result = studentService.updateStudent(1L, studentDetails);

        assertEquals("John Updated", result.getName());
        assertEquals(
                LocalDate.of(2016, 6, 15),
                result.getDateOfBirth()
        );
        assertEquals("Male", result.getGender());

        verify(studentRepository).findById(1L);
        verify(studentRepository).save(existingStudent);
    }


    // 6. UPDATE - Student does not exist
    @Test
    void testUpdateStudentWhenStudentDoesNotExist() {

        Student studentDetails = new Student();
        studentDetails.setName("John Updated");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.updateStudent(1L, studentDetails)
        );

        assertEquals("Student not found with given id: 1", exception.getMessage());

        verify(studentRepository).findById(1L);
        verify(studentRepository, never()).save(any());
    }


    // 7. DELETE
    @Test
    void testDeleteStudent() {

        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }
}