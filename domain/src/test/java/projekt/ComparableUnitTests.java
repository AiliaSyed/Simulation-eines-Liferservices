package projekt;

import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;
import static org.junit.jupiter.api.Assertions.*;


public class ComparableUnitTests<T extends Comparable<? super T>> {

    private final Function<Integer, T> testObjectFactory;

    private T[] testObjects;

    public ComparableUnitTests(Function<Integer, T> testObjectFactory) {
        this.testObjectFactory = testObjectFactory;
    }

    @SuppressWarnings("unchecked")
    public void initialize(int testObjectCount) {
        testObjects = (T[]) new Comparable<?>[testObjectCount];

        for (int i = 0; i < testObjectCount; i++) {
            testObjects[i] = testObjectFactory.apply(i);
        }
        // TODO: H12.1 - remove if implemented
    }

    public void testBiggerThen() {
        for (int i = 0; i < testObjects.length; i++) {
            for (int j = i + 1; j < testObjects.length; j++) {
                T obj1 = testObjects[i];
                T obj2 = testObjects[j];
                assert (obj1.compareTo(obj2) > 0);
            }
        }
        // TODO: H12.1 - remove if implemented
    }

    @SuppressWarnings("EqualsWithItself")
    public void testAsBigAs() {
        for (int i = 0; i < testObjects.length; i++) {
            T obj = testObjects[i];
            assert (obj.compareTo(obj) == 0);
        }
        // TODO: H12.1 - remove if implemented
    }

    public void testLessThen() {
        for (int i = 0; i < testObjects.length; i++) {
            for (int j = i + 1; j < testObjects.length; j++) {
                T obj1 = testObjects[i];
                T obj2 = testObjects[j];
                assert (obj1.compareTo(obj2) < 0);
            }
        }
        // TODO: H12.1 - remove if implemented
    }
}
