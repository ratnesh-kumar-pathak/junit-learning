package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {MvcTestingExampleApplication.class})
public class MockAnnotationTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    CollegeStudent student1;

    @Autowired
    StudentGrades grades;

    // @Mock
    @MockitoBean
    private ApplicationDao applicationDao;

    // @InjectMocks
    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach(){
        student1.setFirstname("Aryan");
        student1.setLastname("Pathak");
        student1.setEmailAddress("arpat@gmail.com");
        student1.setStudentGrades(grades);
    }

    @Test
    @DisplayName("Test for Add Grades")
    public void assertEqualTestAddGrades(){
        when(
              applicationDao.addGradeResultsForSingleClass(
                      grades.getMathGradeResults()
              )
        ).thenReturn(
                    100.00
        );

        assertEquals(
                100,
                applicationService.addGradeResultsForSingleClass(
                        student1.getStudentGrades().getMathGradeResults()
                ),
                "Must Return 100"
        );

        verify(applicationDao)
                .addGradeResultsForSingleClass(
                        grades.getMathGradeResults()
                );
        verify(applicationDao, times(1))
                .addGradeResultsForSingleClass(
                        grades.getMathGradeResults()
                );
    }

    @Test
    @DisplayName("Test for finding GPA")
    void assertEqualsTestFindGpa(){
        when(
                applicationDao.findGradePointAverage(
                        grades.getMathGradeResults()
                )
        ).thenReturn(
                9.2
        );

        assertEquals(
                9.2,
                applicationService.findGradePointAverage(
                        grades.getMathGradeResults()
                ),
                "Must Return 9.2"
        );
    }

    @Test
    @DisplayName("Test for Not Null")
    void testAssertNotNull(){
        when(
                applicationDao.checkNull(
                        grades.getMathGradeResults()
                )
        ).thenReturn(
                true
        );

        assertNotNull(
                applicationService.checkNull(grades.getMathGradeResults()),
                "Must Not Be Null"
        );
    }

    @Test
    @DisplayName("Test for Throwing Exception")
    void testThrowException(){
        CollegeStudent student2 = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(NullPointerException.class).when(
                applicationDao
        ).checkNull(student2);

        assertThrows(
                NullPointerException.class,
                () -> applicationService.checkNull(student2),
                "Must throw NullPointerException"
        );

        verify(applicationDao, times(1)).checkNull(student2);
    }

    @Test
    @DisplayName("Test for stubbing calls")
    void testStubbingExceptions(){
        CollegeStudent student2 = (CollegeStudent) context.getBean("collegeStudent");

        when(
                applicationDao.checkNull(student2)
        ).thenThrow(
                new NullPointerException("Object is Null")
        ).thenReturn(
                "Do not throw anymore exceptions"
        );

        assertThrows(
                NullPointerException.class,
                () -> applicationService.checkNull(student2),
                "Must throw NullPointerException"
        );

        assertEquals(
                "Do not throw anymore exceptions",
                applicationService.checkNull(student2),
                "Must not throw NullPointerException"
        );

        verify(applicationDao, times(2)).checkNull(student2);
    }

}
