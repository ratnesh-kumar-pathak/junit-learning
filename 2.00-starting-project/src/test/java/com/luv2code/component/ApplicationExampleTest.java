package com.luv2code.component;

import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationExampleTest {

    private static int count = 0;

    @Autowired
    private ApplicationContext context;

    @Value("${info.app.name}")
    private String appName;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades grades;

    @BeforeEach
    void beforeEach(){
        count++;

        student.setFirstname("Aryan");
        student.setLastname("Pathak");
        student.setEmailAddress("arpat@gmail.com");

        grades.setMathGradeResults(
                new ArrayList<>(
                        Arrays.asList(90.0, 89.0, 92.50, 100.00)
                )
        );

        student.setStudentGrades(grades);

        System.out.println(appName + "Application Example Test: " + count);
    }

    @Test
    @DisplayName("Test For Adding Grade Results")
    void addGradeResultsEqual() {
        assertEquals(
                371.50,
                grades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults()
                ),
                "Results must be 371.50"
        );
    }

    @Test
    @DisplayName("Test For Adding Grade Results Not Equal")
    void addGradeResultsNotEqual() {
        assertNotEquals(
                400,
                grades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults()
                ),
                "Results must not be 400"
        );
    }

    @Test
    @DisplayName("Test for Greater Grade")
    void isGradeGreater(){
        assertTrue(grades.isGradeGreater(89, 86), "89 is greater than 86");
    }

    @Test
    @DisplayName("Test for Not Greater Grade")
    void isGradeNotGreater(){
        assertFalse(grades.isGradeGreater(79, 85), "79 is not greater than 85");
    }

    @Test
    @DisplayName("Test for null Grades")
    void isGradeNull(){
        assertNotNull(student.getStudentGrades().getMathGradeResults(), "Grades must not be null");
    }

    @Test
    @DisplayName("Test for Creating Student without Grade Init")
    void createStudentWithoutGradeInit(){
        CollegeStudent student2 = context.getBean("collegeStudent", CollegeStudent.class);

        student2.setFirstname("Ratnesh");
        student2.setLastname("Pathak");
        student2.setEmailAddress("rapat@gmail.com");

        assertNotNull(student2.getFirstname());
        assertNotNull(student2.getLastname());
        assertNotNull(student2.getEmailAddress());

        assertNull(student2.getStudentGrades());
    }

    @Test
    @DisplayName("Test for Prototypes")
    void testPrototype(){
        CollegeStudent student2 = context.getBean("collegeStudent", CollegeStudent.class);

        assertNotSame(student, student2);
    }

    @Test
    @DisplayName("Test for Sum and Average")
    void findSumAndAverage(){
        assertAll("Testing for Sum and Average",
                () -> assertEquals(
                        371.50,
                        grades.addGradeResultsForSingleClass(
                                student.getStudentGrades().getMathGradeResults()
                        ),
                        "Result must be 371.50"
                ),
                () -> assertEquals(
                        92.875,
                        grades.findGradePointAverage(
                                student.getStudentGrades().getMathGradeResults()
                        ),
                        "Result must be 92.875"
                )
        );
    }
}













