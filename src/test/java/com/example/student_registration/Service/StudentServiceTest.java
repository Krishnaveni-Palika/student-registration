package com.example.student_registration.Service;

import com.example.student_registration.DTO.StudentRequestDTO;
import com.example.student_registration.DTO.StudentResponseDTO;
import com.example.student_registration.controller.StudentController;
import com.example.student_registration.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;


    // CREATE

    @Test
    void createStudent_shouldReturnCreated_whenRequestIsValid()
            throws Exception {

        StudentRequestDTO request = new StudentRequestDTO();
        request.setStudentId(101);
        request.setName("John");
        request.setDateOfBirth(
                LocalDate.of(2015, 5, 10)
        );
        request.setGender("Male");

        StudentResponseDTO response =
                new StudentResponseDTO(
                        101,
                        "John",
                        LocalDate.of(2015, 5, 10),
                        "Male"
                );

        when(studentService.createStudent(any(StudentRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/student-registration/student")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                        {
                                          "studentId": 101,
                                          "name": "John",
                                          "dateOfBirth": "2015-05-10",
                                          "gender": "Male"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(101))
                .andExpect(jsonPath("$.studentName").value("John"))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(studentService).createStudent(
                any(StudentRequestDTO.class)
        );
    }


    // GET

    @Test
    void getStudentById_shouldReturnStudent_whenStudentExists()
            throws Exception {

        StudentResponseDTO response =
                new StudentResponseDTO(
                        101,
                        "John",
                        LocalDate.of(2015, 5, 10),
                        "Male"
                );

        when(studentService.getStudentById(101))
                .thenReturn(response);

        mockMvc.perform(
                        get("/student-registration/101")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(101))
                .andExpect(jsonPath("$.studentName").value("John"))
                .andExpect(jsonPath("$.dateOfBirth")
                        .value("2015-05-10"))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(studentService).getStudentById(101);
    }


    // GET INVALID ID

    @Test
    void getStudentById_shouldReturnBadRequest_whenIdIsInvalid()
            throws Exception {

        mockMvc.perform(
                        get("/student-registration/0")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }


    // UPDATE

    @Test
    void updateStudent_shouldReturnUpdatedStudent_whenRequestIsValid()
            throws Exception {

        StudentResponseDTO response =
                new StudentResponseDTO(
                        101,
                        "John Updated",
                        LocalDate.of(2015, 5, 10),
                        "Male"
                );

        when(studentService.updateStudent(
                eq(101),
                any(StudentRequestDTO.class)
        )).thenReturn(response);

        mockMvc.perform(
                        patch("/student-registration/101")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                        {
                                          "studentId": 101,
                                          "name": "John Updated",
                                          "dateOfBirth": "2015-05-10",
                                          "gender": "Male"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(101))
                .andExpect(jsonPath("$.studentName")
                        .value("John Updated"))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(studentService).updateStudent(
                eq(101),
                any(StudentRequestDTO.class)
        );
    }


    // UPDATE INVALID ID

    @Test
    void updateStudent_shouldReturnBadRequest_whenIdIsInvalid()
            throws Exception {

        mockMvc.perform(
                        patch("/student-registration/0")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                        {
                                          "studentId": 0,
                                          "name": "John",
                                          "dateOfBirth": "2015-05-10",
                                          "gender": "Male"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }


    // DELETE

    @Test
    void deleteStudent_shouldReturnNoContent_whenStudentExists()
            throws Exception {

        doNothing()
                .when(studentService)
                .deleteStudent(101);

        mockMvc.perform(
                        delete("/student-registration/101")
                )
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(101);
    }


    // DELETE INVALID ID

    @Test
    void deleteStudent_shouldReturnBadRequest_whenIdIsInvalid()
            throws Exception {

        mockMvc.perform(
                        delete("/student-registration/0")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }
}