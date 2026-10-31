package com.luv2code.springmvc;


import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
public class GradebookControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentAndGradeService service;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Value("${sql.scripts.create.student}")
    private String addStudent;

    @Value("${sql.scripts.create.math.grade}")
    private String createMathGrades;

    @Value("${sql.scripts.create.science.grade}")
    private String createScienceGrades;

    @Value("${sql.scripts.create.history.grade}")
    private String createHistoryGrades;

    @BeforeAll
    static void setup() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Aryan");
        request.setParameter("lastname", "Pathak");
        request.setParameter("emailAddress", "arpat@gmail.com");
    }

    @BeforeEach
    void beforeEach() {
        jdbc.execute(addStudent);

        jdbc.execute(createMathGrades);
        jdbc.execute(createScienceGrades);
        jdbc.execute(createHistoryGrades);
    }

    @Test
    void getHttpRequest() throws Exception {
        List<CollegeStudent> list = new ArrayList<>(
                Arrays.asList(
                        new CollegeStudent(
                                "Aryan",
                                "Pathak",
                                "arpat@gmail.com"
                        ),
                        new CollegeStudent(
                                "Ratnesh",
                                "Pathak",
                                "rapat@gmail.com"
                        )
                )
        );

        when(service.getGradeBook()).thenReturn(list);

        assertIterableEquals(list, service.getGradeBook());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");
    }

    @Test
    void createStudentHttpRequest() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", request.getParameterValues("firstname"))
                .param("lastname", request.getParameterValues("lastname"))
                .param("emailAddress", request.getParameterValues("emailAddress"))
        ).andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        assertNotNull(studentDao.findByEmailAddress("arpat@gmail.com"), "Student must not be null");
    }

    @Test
    void deleteStudentHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        assertTrue(mathGradesDao.findById(1).isPresent());
        assertTrue(scienceGradesDao.findById(1).isPresent());
        assertTrue(historyGradeDao.findById(1).isPresent());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        assertFalse(studentDao.findById(1).isPresent());

        assertFalse(mathGradesDao.findById(1).isPresent());
        assertFalse(scienceGradesDao.findById(1).isPresent());
        assertFalse(historyGradeDao.findById(1).isPresent());
    }

    @Test
    void deleteStudentErrorTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void createGradeService() {
        assertTrue(service.createGrade(91.50, 1, "math"), "Grade must be added");
        assertTrue(service.createGrade(88.00, 1, "science"), "Grade must be added");
        assertTrue(service.createGrade(96.35, 1, "history"), "Grade must be added");

        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);

        assertEquals(2, ((Collection<MathGrade>) mathGrades).size());
        assertEquals(2, ((Collection<ScienceGrade>) scienceGrades).size());
        assertEquals(2, ((Collection<HistoryGrade>) historyGrades).size());
    }

    @Test
    void createGradeServiceReturnFalse() {
        assertFalse(service.createGrade(101.50, 1, "math"));
        assertFalse(service.createGrade(-88.00, 1, "history"));
        assertFalse(service.createGrade(96.35, 2, "science"));
        assertFalse(service.createGrade(96.35, 1, ",kguluig"));
    }

    @Test
    void deleteGradeTest() {
        int sz = ((Collection<MathGrade>)mathGradesDao.findGradeByStudentId(1)).size();
        assertEquals(1, service.deleteGrade(1, "math"));
        assertEquals(sz - 1, ((Collection<MathGrade>)mathGradesDao.findGradeByStudentId(1)).size());

        assertEquals(1, service.deleteGrade(1, "history"));
        assertEquals(1, service.deleteGrade(1, "science"));

        assertNotEquals(1, service.deleteGrade(1, "math"));
    }

    @Test
    void getStudentInformation() {
        GradebookCollegeStudent info = service.studentInformation(1);

        assertNotNull(info);
        assertEquals("Aryan", info.getFirstname());
        assertEquals("Pathak", info.getLastname());
        assertEquals("arry@gmail.com", info.getEmailAddress());
        assertEquals(1, info.getStudentGrades().getMathGradeResults().size());
        assertEquals(1, info.getStudentGrades().getScienceGradeResults().size());
        assertEquals(1, info.getStudentGrades().getHistoryGradeResults().size());

        assertNull(service.studentInformation(0));
    }

    @Test
    void studentInformationHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/studentInformation/{id}", 1)
        ).andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "studentInformation");

        assertFalse(studentDao.findById(0).isPresent());

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/studentInformation/{id}", 0)
        ).andExpect(status().isOk()).andReturn();

        mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void createStudentGradeHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        GradebookCollegeStudent info = service.studentInformation(1);
        assertEquals(1,  info.getStudentGrades().getMathGradeResults().size());

        MvcResult mvcResult = mockMvc.perform(post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grade", "86.80")
                .param("gradeType", "math")
                .param("studentId", "1")
        ).andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "studentInformation");

        info = service.studentInformation(1);
        assertEquals(2,  info.getStudentGrades().getMathGradeResults().size());

        mvcResult = mockMvc.perform(post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grade", "86.80")
                .param("gradeType", "math")
                .param("studentId", "1")
        ).andExpect(status().isOk()).andReturn();

        mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    void deleteGradeHttpRequest() throws Exception {
        assertTrue(mathGradesDao.findById(1).isPresent());

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 1, "math")
        ).andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "studentInformation");

        assertFalse(mathGradesDao.findById(1).isPresent());
    }

    @AfterEach
    void tearDown() {
        jdbc.execute("delete from student");

        jdbc.execute("delete from math_grade");
        jdbc.execute("delete from history_grade");
        jdbc.execute("delete from science_grade");

        jdbc.execute("alter table student alter column id restart with 1");

        jdbc.execute("alter table math_grade alter column id restart with 1");
        jdbc.execute("alter table history_grade alter column id restart with 1");
        jdbc.execute("alter table science_grade alter column id restart with 1");
    }
}























