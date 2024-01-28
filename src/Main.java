public class Main {
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        runner.runTests(TestOneClass.class);
//        runner.runTests(TestTwoClass.class); test with exception: RuntimeException: There can be only one method with annotation BeforeSuite
//        runner.runTests(TestThreeClass.class); test with exception: RuntimeException: @AfterSuite method must be static
//        runner.runTests(TestFourClass.class); test with exception: IllegalArgumentException: wrong number of arguments
    }
}