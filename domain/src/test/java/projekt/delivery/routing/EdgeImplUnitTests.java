package projekt.delivery.routing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;

import static org.tudalgo.algoutils.student.Student.crash;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeImplUnitTests {

    private static ComparableUnitTests<Region.Edge> comparableUnitTests;
    private static ObjectUnitTests<Region.Edge> objectUnitTests;
    private static NodeImpl nodeA;
    private static NodeImpl nodeB;
    private static NodeImpl nodeC;

    private static EdgeImpl edgeAA;
    private static EdgeImpl edgeAB;
    private static EdgeImpl edgeBC;

    @BeforeAll
    public static void initialize() {
        crash();
        // TODO: H12.5 - remove if implemented
    }

    @Test
    public void testEquals() {
        objectUnitTests.testEquals(); // TODO: H12.5 - remove if implemented
    }

    @Test
    public void testHashCode() {
        objectUnitTests.testHashCode(); // TODO: H12.5 - remove if implemented
    }

    @Test
    public void testToString() {
        objectUnitTests.testToString(); // TODO: H12.5 - remove if implemented
    }

    @Test
    public void testBiggerThen() {
        comparableUnitTests.testBiggerThen();// TODO: H12.5 - remove if implemented
    }

    @Test
    public void testAsBigAs() {
        comparableUnitTests.testAsBigAs();// TODO: H12.5 - remove if implemented
    }

    @Test
    public void testLessThen() {
        comparableUnitTests.testLessThen();
        // TODO: H12.5 - remove if implemented
    }

    @Test
    public void testGetNode() {
        crash();
    // TODO: H12.5 - remove if implemented
    }
}
