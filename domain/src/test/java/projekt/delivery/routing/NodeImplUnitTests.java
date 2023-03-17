package projekt.delivery.routing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;
import projekt.base.Location;

import javax.naming.spi.ObjectFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;
import static org.junit.jupiter.api.Assertions.*;

public class NodeImplUnitTests {

    private static ComparableUnitTests<NodeImpl> comparableUnitTests;
    private static ObjectUnitTests<NodeImpl> objectUnitTests;
    private static NodeImpl nodeA;
    private static NodeImpl nodeB;
    private static NodeImpl nodeC;
    private static NodeImpl nodeD;

    private static EdgeImpl edgeAA;
    private static EdgeImpl edgeAB;
    private static EdgeImpl edgeBC;

    @BeforeAll
    public static void initialize() {
        crash();
        // TODO: H12.4 - remove if implemented
    }

    @Test
    public void testEquals() {
        objectUnitTests.testEquals();
        // TODO: H12.4 - remove if implemented
    }

    @Test
    public void testHashCode() {
        objectUnitTests.testHashCode();// TODO: H12.4 - remove if implemented
    }

    @Test
    public void testToString() {
        objectUnitTests.testToString();
        // TODO: H12.4 - remove if implemented
    }

    @Test
    public void testBiggerThen() {
        comparableUnitTests.testBiggerThen();// TODO: H12.4 - remove if implemented
    }

    @Test
    public void testAsBigAs() {
        comparableUnitTests.testAsBigAs();// TODO: H12.4 - remove if implemented
    }

    @Test
    public void testLessThen() {
        comparableUnitTests.testLessThen();// TODO: H12.4 - remove if implemented
    }

    @Test
    public void testGetEdge() {
        comparableUnitTests.testLessThen(); // TODO: H12.4 - remove if implemented
    }

    @Test
    public void testAdjacentNodes() {
        Set<NodeImpl> adjacentNodesA = new HashSet<>();
        adjacentNodesA.add(nodeB);
        adjacentNodesA.add(nodeC);
        assertEquals(adjacentNodesA, nodeA.getAdjacentNodes());

        Set<NodeImpl> adjacentNodesB = new HashSet<>();
        adjacentNodesB.add(nodeA);
        adjacentNodesB.add(nodeC);
        assertEquals(adjacentNodesB, nodeB.getAdjacentNodes());

        Set<NodeImpl> adjacentNodesC = new HashSet<>();
        adjacentNodesC.add(nodeA);
        adjacentNodesC.add(nodeB);
        assertEquals(adjacentNodesC, nodeC.getAdjacentNodes());

        Set<NodeImpl> adjacentNodesD = new HashSet<>();
        assertEquals(adjacentNodesD, nodeD.getAdjacentNodes());
        // TODO: H12.4 - remove if implemented
    }

    @Test
    public void testAdjacentEdges() {
        Set<NodeImpl> adjacentEdgesA = new HashSet<>();
        adjacentEdgesA.add(nodeB);
        adjacentEdgesA.add(nodeC);
        assertEquals(adjacentEdgesA, nodeA.getAdjacentEdges());

        Set<NodeImpl> adjacentEdgesB = new HashSet<>();
        adjacentEdgesB.add(nodeA);
        adjacentEdgesB.add(nodeC);
        assertEquals(adjacentEdgesB, nodeB.getAdjacentEdges());

        Set<NodeImpl> adjacentEdgesC = new HashSet<>();
        adjacentEdgesC.add(nodeA);
        adjacentEdgesC.add(nodeB);
        assertEquals(adjacentEdgesC, nodeC.getAdjacentEdges());

        Set<NodeImpl> adjacentEdgesD = new HashSet<>();
        assertEquals(adjacentEdgesD, nodeD.getAdjacentEdges());
        // TODO: H12.4 - remove if implemented
    }
}
