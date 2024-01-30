package test.implementation;

import test.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestRunner {


    public void runTests(Class<?> cls)  {
        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> beforeTestMethods = new ArrayList<>();
        List<Method> afterTestMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        Method[] methods = cls.getDeclaredMethods();

        if (methods.length == 0) {
            return;
        }
        System.out.println("Start testing for class " + cls.getSimpleName());
        for (Method m : methods) {
            try {
                if (m.isAnnotationPresent(BeforeSuite.class)) {
                    if (!Modifier.isStatic(m.getModifiers())) {
                        throw new RuntimeException("@AfterSuite method must be static ");
                    }
                    if (beforeSuiteMethod == null) {
                        beforeSuiteMethod = m;
                    } else throw new RuntimeException("There can be only one method with annotation BeforeSuite");
                }
                if (m.isAnnotationPresent(BeforeTest.class)) {
                    beforeTestMethods.add(m);
                }

                if (m.isAnnotationPresent(AfterTest.class)) {
                    afterTestMethods.add(m);
                }
                if (m.isAnnotationPresent(AfterSuite.class)) {
                    if (!Modifier.isStatic(m.getModifiers())) {
                        throw new RuntimeException("Test method " + m + " failed. Cause: " +
                                "@AfterSuite method must be static ");
                    }
                    if (afterSuiteMethod == null) {
                        afterSuiteMethod = m;
                    } else throw new RuntimeException("Test method " + m + " failed. Cause: " +
                            "There can be only one method with annotation AfterSuite");
                }
                if (m.isAnnotationPresent(Test.class)) {
                    checkAndAddMethods(m, testMethods);
                }
            } catch (Exception e) {
                errors.add(e.getMessage());
            }
        }

        Collections.sort(testMethods, Comparator.comparingInt(TestRunner::getPriority));
        try {
            if (beforeSuiteMethod != null) {
                beforeSuiteMethod.invoke(cls.getDeclaredConstructor().newInstance());
            }
            for (Method m: testMethods) {
                Object instance = cls.getDeclaredConstructor().newInstance();
                for (Method before : beforeTestMethods) {
                    before.invoke(instance);
                }

                if (m.isAnnotationPresent(Test.class)){
                    if (m.isAnnotationPresent(CsvSource.class)) {
                        Object[] arguments = parseCsvSource(m);
                        m.invoke(instance, arguments);
                    } else m.invoke(instance);
                }
                for (Method after: afterTestMethods) {
                    after.invoke(instance);
                }
            }
            if (afterSuiteMethod != null) {
                afterSuiteMethod.invoke(cls.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        }
        printErrors(errors);
        System.out.println("\nEnd testing for class " + cls.getSimpleName() + " \n \n");
    }

    private static void checkAndAddMethods(Method m, List<Method> testMethods) {
        int priority = getPriority(m);
        if (priority < 1 || priority > 10) {
            throw new RuntimeException("Test " + m + " failed. Cause: " +
                    "priority must by more from 1 and until 1");
        }
        testMethods.add(m);
    }

    private static void printErrors(List<String> errors) {
        if (errors != null && errors.size() > 0) {
            System.out.println("\n Errors:");
            errors.forEach(System.out::println);
        }
    }

    private static int getPriority(Method m) {
        return m.getAnnotation(Test.class).priority();
    }

    private static Object[] parseCsvSource(Method method) {
        String csvSource = method.getAnnotation(CsvSource.class).value();
        // Parse CSV string and convert each element to the appropriate type
        if (csvSource.isBlank() || csvSource.isEmpty()) {
            return null;
        }
        String[] elements = csvSource.split(",");
        Object[] arguments = new Object[elements.length];
        for (int i = 0; i < elements.length; i++) {
            String element = elements[i].trim();
            if (element.matches("\\d+")) {
                arguments[i] = Integer.parseInt(element);
            } else if (element.equalsIgnoreCase("true") || element.equalsIgnoreCase("false")) {
                arguments[i] = Boolean.parseBoolean(element);
            } else {
                arguments[i] = element;
            }
        }
        if (method.getParameterCount() != arguments.length) {
            throw new RuntimeException("Incorrect count of parameters for annotation @CsvSource " +
                    " for method " + method);
        }
        return arguments;
    }
}
