import domain.TestOneClass;
import test.implementation.TestRunner;

public class Main {
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        runner.runTests(TestOneClass.class);

//        test with exception: RuntimeException: There can be only one method with annotation test.annotation.BeforeSuite
        runner.runTests(domain.TestTwoClass.class);

//        test with exception: RuntimeException: @test.annotation.AfterSuite method must be static
        runner.runTests(domain.TestThreeClass.class);

//        test with exception: IllegalArgumentException: wrong number of arguments
        runner.runTests(domain.TestFourClass.class);
    }
}