package domain;

import test.annotation.*;

public class TestOneClass {

    @BeforeTest
    public void beforeTest() {
        System.out.println("Before Test");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("After Test");
    }
    @BeforeSuite
    public static void setup() {
        System.out.println("BeforeSuite");
    }

    @Test(priority = 9)
    public void test1() {
        System.out.println("Test 1");
    }

    @Test(priority = -1)
    public void testMinus() {
        System.out.println("Test wrong");
    }

    @Test(priority = 2)
    public void test2() {
        System.out.println("Test 2");
    }

    @Test
    public void test3() {
        System.out.println("Test 3");
    }

    @AfterSuite
    public static void cleanup() {
        System.out.println("AfterSuite");
    }
    @CsvSource("")
    @Test
    public void testCsv() {
        System.out.println("Test csv without args");
    }
    @CsvSource("word,34")
    @Test
    public void testCsvWithArgs(String string, int a) {
        System.out.println("Test csv with args: \n string = " + string + "\n a = " + a);
    }
    @CsvSource("start,34,11,end")
    @Test
    public void testCsvWithArgs(String string1, int a, int b, String string2) {
        System.out.println("Test csv with args: \n string1 = " + string1 + "\n a = " + a +" \n string2 = " + string2 + "\n b = " + b);
    }
}
