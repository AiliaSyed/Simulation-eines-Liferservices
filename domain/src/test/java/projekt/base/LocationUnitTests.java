package projekt.base;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;

import java.util.Objects;
import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;
import static org.junit.jupiter.api.Assertions.*;

public class LocationUnitTests {

    private static ComparableUnitTests<Location> comparableUnitTests;
    private static ObjectUnitTests<Location> objectUnitTests;

    @BeforeAll
    public static void initialize() {
        // Initialize testObjectFactory with a lambda that creates Location objects
        Function<Integer, Location> testObjectFactory = i -> new Location(i, i + 1);
        // Initialize objectUnitTests and comparableUnitTests with testObjectFactory and a toString function
        objectUnitTests = new ObjectUnitTests<>(testObjectFactory, Location::toString);
        comparableUnitTests = new ComparableUnitTests<>(testObjectFactory);

        // Initialize objectUnitTests and comparableUnitTests with 10 test objects
        objectUnitTests.initialize(10);
        comparableUnitTests.initialize(10);

        // TODO: H12.2 - remove if implemented
    }
    @Test
    public void testEquals() {
        objectUnitTests.testEquals();
        // TODO: H12.2 - remove if implemented
    }

    @Test
    public void testHashCode() {
        objectUnitTests.testHashCode();
        // TODO: H12.2 - remove if implemented
    }

    @Test
    public void testToString() {
        objectUnitTests.testToString();
        // TODO: H12.2 - remove if implemented
    }

    @Test
    public void testBiggerThen() {
        comparableUnitTests.testBiggerThen();
        // TODO: H12.2 - remove if implemented
    }

    @Test
    public void testAsBigAs() {
        comparableUnitTests.testAsBigAs();
        // TODO: H12.2 - remove if implemented
    }

    @Test
    public void testLessThen() {
        comparableUnitTests.testLessThen();
        // TODO: H12.2 - remove if implemented
    }

}
