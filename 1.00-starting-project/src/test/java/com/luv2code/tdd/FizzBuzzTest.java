package com.luv2code.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

    @Test
    @Order(1)
    @DisplayName("Test for Divisible By Three")
    void testForDivisibleByThree() {
        String expected = "Fizz";
        assertEquals(expected, FizzBuzz.compute(3), "Must Return Fizz");
    }

    @Test
    @Order(2)
    @DisplayName("Test for Divisible By Five")
    void testForDivisibleByFive() {
        String expected = "Buzz";
        assertEquals(expected, FizzBuzz.compute(5), "Must Return Buzz");
    }

    @Test
    @Order(3)
    @DisplayName("Test for Divisible By Three and Five")
    void testForDivisibleByThreeAndFive() {
        String expected = "FizzBuzz";
        assertEquals(expected, FizzBuzz.compute(15), "Must Return FizzBuzz");
    }

    @Test
    @Order(4)
    @DisplayName("Test for Not Divisible By Three or Five")
    void testForNotDivisibleByThreeOrFive() {
        String expected = "4";
        assertEquals(expected, FizzBuzz.compute(4), "Must Return 4");
    }

    @ParameterizedTest(name = "value={0}, expected={1}")
    @CsvFileSource(resources = "/small-test-data.csv")
    @Order(5)
    @DisplayName("Testing with Small Data File")
    void testForSmallDataFile(int value, String expected) {
        assertEquals(expected, FizzBuzz.compute(value), "Must Return " + expected);
    }

    @ParameterizedTest(name = "value={0}, expected={1}")
    @CsvFileSource(resources = "/medium-test-data.csv")
    @Order(6)
    @DisplayName("Testing with Medium Data File")
    void testForMediumDataFile(int value, String expected) {
        assertEquals(expected, FizzBuzz.compute(value), "Must Return " + expected);
    }

    @ParameterizedTest(name = "value={0}, expected={1}")
    @CsvFileSource(resources = "/large-test-data.csv")
    @Order(7)
    @DisplayName("Testing with Large Data File")
    void testForLargeDataFile(int value, String expected) {
        assertEquals(expected, FizzBuzz.compute(value), "Must Return " + expected);
    }
}













