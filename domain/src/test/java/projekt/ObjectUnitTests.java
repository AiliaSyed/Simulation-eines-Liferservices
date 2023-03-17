package projekt;

import org.junit.jupiter.api.Assertions;

import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectUnitTests<T> {

    private final Function<Integer, T> testObjectFactory;
    private final Function<T, String> toString;

    private T[] testObjects;
    private T[] testObjectsReferenceEquality;
    private T[] testObjectsContentEquality;

    public ObjectUnitTests(Function<Integer, T> testObjectFactory, Function<T, String> toString) {
        this.testObjectFactory = testObjectFactory;
        this.toString = toString;
    }

    @SuppressWarnings("unchecked")
    public void initialize(int testObjectCount) {
        testObjects = (T[]) new Object[testObjectCount];
        testObjectsReferenceEquality = (T[]) new Object[testObjectCount];
        testObjectsContentEquality = (T[]) new Object[testObjectCount];
        for (int i = 0; i < testObjectCount; i++) {
            testObjects[i] = testObjectFactory.apply(i);
            testObjectsReferenceEquality[i] = testObjects[i];
            testObjectsContentEquality[i] = testObjectFactory.apply(i);
        }
        // TODO: H12.1 - remove if implemented
    }

    public void testEquals() {
        // test reference equality
        for (int i = 0; i < testObjects.length; i++) {
            Assertions.assertEquals(testObjects[i], testObjectsReferenceEquality[i]);
        }
        // test content equality
        for (int i = 0; i < testObjects.length; i++) {
            Assertions.assertEquals(testObjects[i], testObjectsContentEquality[i]);
        }
        // test null equality
        Assertions.assertNotEquals(null, testObjects[0]);
        // test inequality
        for (int i = 0; i < testObjects.length; i++) {
            for (int j = i + 1; j < testObjects.length; j++) {
                Assertions.assertNotEquals(testObjects[i], testObjects[j]);
            }
        }
        // TODO: H12.1 - remove if implemented
    }

    public void testHashCode() {
        for (int i = 0; i < testObjects.length; i++) {
            for (int j = 0; j < testObjects.length; j++) {
                if (i == j) {
                    Assertions.assertEquals(testObjects[i].hashCode(), testObjectsReferenceEquality[j].hashCode());
                    Assertions.assertEquals(testObjects[i].hashCode(), testObjectsContentEquality[j].hashCode());
                } else {
                    Assertions.assertNotEquals(testObjects[i].hashCode(), testObjects[j].hashCode());
                }
            }
        }
        // TODO: H12.1 - remove if implemented
    }

    public void testToString() {
        for (int i = 0; i < testObjects.length; i++) {
            String expected = toString.apply(testObjects[i]);
            Assertions.assertEquals(expected, testObjects[i].toString());
        }
        // TODO: H12.1 - remove if implemented
    }

}
