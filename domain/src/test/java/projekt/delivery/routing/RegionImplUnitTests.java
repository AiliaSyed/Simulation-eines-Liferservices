package projekt.delivery.routing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;
import projekt.base.Location;

import java.util.HashSet;
import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;
import static org.junit.jupiter.api.Assertions.*;


public class RegionImplUnitTests {

    private static ObjectUnitTests<RegionImpl> objectUnitTests;

    @BeforeAll
    public static void initialize() {
        objectUnitTests = new ObjectUnitTests(null, null);

        // Initialize objectUnitTests and comparableUnitTests with 10 test objects
        objectUnitTests.initialize(10);

        // TODO: H12.3 - remove if implemented
    }

    @Test
    public void testEquals() {
        RegionImpl sameRegion = new RegionImpl(null);
        RegionImpl differentRegion = new RegionImpl(null);

        assertTrue(region.equals(sameRegion));
        assertFalse(region.equals(differentRegion));
        // TODO: H12.3 - remove if implemented
    }

    @Test
    public void testHashCode() {
        RegionImpl sameRegion = new RegionImpl(null);
        RegionImpl differentRegion = new RegionImpl(null);

        assertEquals(region.hashCode(), sameRegion.hashCode());
        assertNotEquals(region.hashCode(), differentRegion.hashCode());

        // TODO: H12.3 - remove if implemented
    }

    RegionImpl region;
    @Test
    public void testNodes() {
       crash();
        // TODO: H12.3 - remove if implemented
    }



    @Test
    public void testEdges() {

        crash();
        // TODO: H12.3 - remove if implemented
    }
}
