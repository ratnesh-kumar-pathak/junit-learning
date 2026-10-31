package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ConditionalTests {

    @Test
    @Disabled("Run only when xyz is resolved")
    void basicTest() {

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindows() {

    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMac() {

    }

    @Test
    @EnabledOnOs({OS.WINDOWS, OS.LINUX})
    void testForWindowsAndLinux(){

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "env" , matches = "DEV_ENV")
    void testForDevEnvironment() {

    }

    @Test
    @EnabledIfSystemProperty(named = "prop", matches = "SYS_PROP")
    void testForSystemProperty() {

    }
}
