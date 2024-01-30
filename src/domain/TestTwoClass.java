package domain;

import test.annotation.AfterSuite;
import test.annotation.BeforeSuite;
import test.annotation.Test;

public class TestTwoClass {

    @BeforeSuite
    public static void setup() {
        System.out.println("BeforeSuite");
    }

    @Test(priority = 9)
    public void test1() {
        System.out.println("Test 1");
    }

    @Test(priority = 2)
    public void test2() {
        System.out.println("Test 2");
    }

    @Test(priority = 8)
    public void test3() {
        System.out.println("Test 1");
    }

    @Test(priority = 4)
    public void test4() {
        System.out.println("Test 2");
    }

    @Test
    public void test5() {
        System.out.println("Test 3");
    }

    @AfterSuite
    public static void cleanup() {
        System.out.println("AfterSuite");
    }

    @AfterSuite
    public static void cleanupTwo() {
        System.out.println("AfterSuite");
    }
}
