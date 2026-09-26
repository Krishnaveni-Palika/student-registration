package com.example.student_registration.Controller;

import com.example.student_registration.controller.StudentController;
import com.example.student_registration.entity.Student;
import com.example.student_registration.service.StudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(studentController)
                .build();
    }

    @Test
    void createStudentTest() throws Exception {

        Student student = new Student();
        student.setName("John");
        student.setDateOfBirth(LocalDate.of(2016, 5, 10));
        student.setGender("Male");

        when(studentService.createStudent(any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(post("/student/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(studentService).createStudent(any(Student.class));
    }

    @Test
    void getStudentByIdReturnStudentTest() throws Exception {

        Student student = new Student();
        student.setName("John");
        student.setDateOfBirth(LocalDate.of(2016, 5, 10));
        student.setGender("Male");

        when(studentService.getStudentById(1L))
                .thenReturn(student);

        mockMvc.perform(get("/student/getStudentById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(studentService).getStudentById(1L);
    }

    @Test
    void updateStudentReturnUpdatedStudentTest() throws Exception {

        Student student = new Student();
        student.setName("John Updated");
        student.setDateOfBirth(LocalDate.of(2016, 5, 10));
        student.setGender("Male");

        when(studentService.updateStudent(eq(1L), any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(put("/student/updateStudent/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(studentService)
                .updateStudent(eq(1L), any(Student.class));
    }

    @Test
    void deleteStudentDeletesStudentTest() throws Exception {

        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/student/deleteStudent/1"))
                .andExpect(status().isOk());

        verify(studentService).deleteStudent(1L);
    }
}