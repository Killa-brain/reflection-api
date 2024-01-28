import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestRunner {

    public void runTests(Class<?> cls) throws RuntimeException {
        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> beforeTestMethods = new ArrayList<>();
        List<Method> afterTestMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        Method[] methods = cls.getDeclaredMethods();

        for (Method m : methods) {
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
                    throw new RuntimeException("@AfterSuite method must be static ");
                }
                if (afterSuiteMethod  == null) {
                    afterSuiteMethod = m;
                } else throw new RuntimeException("There can be only one method with annotation AfterSuite");
            }
            if (m.isAnnotationPresent(Test.class)) {
                testMethods.add(m);
            }
            if (m.isAnnotationPresent(CsvSource.class)) {
                testMethods.add(m);
            }
        }

        Collections.sort(testMethods, Comparator.comparingInt(m -> getPriority(m)));
        try {
            if (beforeSuiteMethod != null) {
                beforeSuiteMethod.invoke(cls.getDeclaredConstructor().newInstance());
            }
            for (Method m: testMethods) {
                Object instance = cls.getDeclaredConstructor().newInstance();
                for (Method before : beforeTestMethods) {
                    before.invoke(instance);
                }
                if (m.isAnnotationPresent(CsvSource.class)) {
                    Object[] arguments = parseCsvSource(m.getAnnotation(CsvSource.class).value());
                    m.invoke(instance, arguments);
                }
                if (m.isAnnotationPresent(Test.class)){
                    m.invoke(instance);
                }
                for (Method after: afterTestMethods) {
                    after.invoke(instance);
                }
            }
            if (afterSuiteMethod != null) {
                afterSuiteMethod.invoke(cls.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getPriority(Method m) {
        return m.isAnnotationPresent(Test.class) ? m.getAnnotation(Test.class).priority() : 5;
    }

    private static Object[] parseCsvSource(String csvSource) {
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
        return arguments;
    }
}
