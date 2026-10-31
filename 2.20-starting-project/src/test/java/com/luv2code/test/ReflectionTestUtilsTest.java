package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ReflectionTestUtilsTest {
    @Autowired
    private ApplicationContext context;

    @Autowired
    CollegeStudent student1;

    @Autowired
    StudentGrades grades;

    @BeforeEach
    public void beforeEach(){
        student1.setFirstname("Aryan");
        student1.setLastname("Pathak");
        student1.setEmailAddress("arpat@gmail.com");
        student1.setStudentGrades(grades);

        ReflectionTestUtils.setField(
                student1,
                "id",
                1
        );
        ReflectionTestUtils.setField(
                student1,
                "studentGrades",
                new StudentGrades(
                        new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75))
                )
        );
    }

    @Test
    @DisplayName("Test for getting private fields")
    void testGetPrivateFields() {
        assertEquals(
                1,
                ReflectionTestUtils.getField(student1, "id"),
                "ID must be 1"
        );
    }

    @Test
    @DisplayName("Test for invoking private methods")
    void invokePrivateMethods() {
        assertEquals(
                "1: Aryan",
                ReflectionTestUtils.invokeMethod(student1, "getFirstnameAndId"),
                "Fail: Private method not called"
        );
    }
}














