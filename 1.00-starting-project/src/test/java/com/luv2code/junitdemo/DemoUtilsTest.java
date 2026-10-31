package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setUp() {
        demoUtils = new DemoUtils();
    }


    @Test
    @DisplayName("Test for Equality")
    @Order(1)
    void test_Equals_And_Not_Equals() {
        System.out.println("testEqualsAndNotEquals running.....");

        assertEquals(7, demoUtils.add(3,4), "3 + 4 must be 7");
        assertNotEquals(11, demoUtils.add(3,4), "3 + 4 must not be 11");
    }

    @Test
    @DisplayName("Test for Null")
    @Order(2)
    void test_Null_And_Not_Null() {
        System.out.println("testNullAndNotNull running.....");

        assertNull(demoUtils.checkNull(null), "Must be null");
        assertNotNull(demoUtils.checkNull("Boom"), "Must not be null");
    }

    @Test
    @DisplayName("Test for Same and not Same")
    @Order(3)
    void testSameAndNotSame() {
        String str = new String("Luv2Code Academy");

        assertSame(
                demoUtils.getAcademy(),
                demoUtils.getAcademyDuplicate(),
                "Both objects must refer to the same string");
        ;
        assertNotSame(
                str,
                demoUtils.getAcademy(),
                "Both objects must not refer to the same string"
        );
    }

    @Test
    @DisplayName("Test for True and False")
    @Order(4)
    void testTrueFalse(){
        assertTrue(demoUtils.isGreater(8, 2), "8 is greater than 2");
        assertFalse(demoUtils.isGreater(3, 6), "3 is not greater than 6");
    }

    @Test
    @DisplayName("Test for Array Equality")
    @Order(5)
    void testArrayEquality(){
        assertArrayEquals(
                new String[] {"A", "B", "C"},
                demoUtils.getFirstThreeLettersOfAlphabet(),
                "Arrays must be equal"
        );
    }

    @Test
    @DisplayName("Test for Iterables Equality")
    @Order(6)
    void testIterableEquality(){
        assertIterableEquals(
                demoUtils.getAcademyInList(),
                List.of("luv", "2", "code"),
                "Iterables must be equal"
        );
    }

    @Test
    @DisplayName("Test for Lines Match")
    @Order(7)
    void testLinesMatch(){
        assertLinesMatch(
                List.of("luv", ">>>>", "code"), // >>>> means skip zero or more lines until next match
                demoUtils.getAcademyInList(),
                "Lines must match"
        );
    }

    @Test
    @DisplayName("Test for Exceptions")
    @Order(8)
    void testThrowsAndDoesNotThrow(){
        assertThrows(
                Exception.class,
                () -> { demoUtils.throwException(-2); },
                "Throws exception for negative inputs"
        );
        assertDoesNotThrow(
                () -> { demoUtils.throwException(2); },
                "Throws exception for negative inputs"
        );
    }

    @Test
    @DisplayName("Test for Timeout")
    @Order(9)
    void testForTimeout(){
        assertTimeoutPreemptively(
                Duration.ofSeconds(3),
                () -> { demoUtils.checkTimeout(); },
                "Execution must finish within 3 seconds"
        );
    }

    @Test
    @DisplayName("Test for multiply")
    @Order(10)
    void testMultiply(){
        assertEquals(21, demoUtils.multiply(3,7), "3 * 7 must be 21");
        assertNotEquals(22, demoUtils.multiply(3,7), "3 * 7 must not be 22");
    }

/*
    @BeforeAll
    static void beforeAll() {
        System.out.println("-----------------------------@BeforeAll---------------------------");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test Finished");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("--------------------------------@AfterAll------------------------------------");
    }
 */
}
