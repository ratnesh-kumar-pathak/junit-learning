package com.luv2code.springmvc;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application.properties")
public class StudentAndGradeServiceTest {

    @Autowired
    private StudentDao studentDAO;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    public void setup() {
        jdbc.execute(
                "insert into student(firstname, lastname, email_address) "
                + "values ('Aryan', 'Pathak', 'arpat@gmail.com')"
        );
    }

    @Test
    void testCreateStudent() {
        // studentService.createStudent("Aryan", "Pathak", "arpat@gmail.com");

        CollegeStudent student = studentDAO.findByEmailAddress("arpat@gmail.com");

        assertEquals("arpat@gmail.com", student.getEmailAddress(), "Find student");
    }

    @Test
    void isStudentNull(){
        assertNull(studentService.isStudentNull(0), "Student with id 0 must be null");
        assertNotNull(studentService.isStudentNull(1), "Student with id 1 must not be null");
    }

    @Test
    void deleteStudent(){
        Optional<CollegeStudent> collegeStudent = studentDAO.findById(1);
        assertTrue(collegeStudent.isPresent());

        studentService.deleteStudent(1);

        collegeStudent = studentDAO.findById(1);
        assertFalse(collegeStudent.isPresent());
    }

    @Test
    @Sql("/insertData.sql")
    void getAllStudents(){
        List<CollegeStudent> students = studentService.getGradeBook();
        assertEquals(5, students.size());
    }

    @AfterEach
    public void teardown() {
        jdbc.execute("delete from student");
        jdbc.execute("alter table student alter column id restart with 1");
    }

}























