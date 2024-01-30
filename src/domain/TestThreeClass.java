package domain;

import test.annotation.AfterSuite;

public class TestThreeClass {

    @AfterSuite
    public void cleanupTwo() {
        System.out.println("AfterSuite");
    }
}
