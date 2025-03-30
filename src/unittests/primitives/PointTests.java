package unittests.primitives;

import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Point} class.
 * This class tests the functionality of basic operations on points,
 * such as addition, subtraction, and distance calculations.
 */
public class PointTests {

    /**
     * Test the {@link Point#add(Vector)} method.
     * Ensures that adding a vector to a point results in the correct new point.
     */
    @Test
    public void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Point point = new Point(1,1,1);
        Vector vector = new Vector(2,2,2);
        assertEquals(
                new Point(3,3,3),
                point.add(vector),
                "The result should be Point(3,3,3)"
        );
    }

    /**
     * Test the {@link Point#subtract(Point)} method.
     * Ensures correct subtraction results and exception handling for identical points.
     */
    @Test
    public void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtracting two different points should result in a vector.
        Point point1 = new Point(1,1,1);
        Point point2 = new Point(2,2,2);
        assertEquals(
                new Vector(-1,-1,-1),
                point1.subtract(point2),
                "The result should be Vector(-1,-1,-1)"
        );

        // =============== Boundary Values Tests ==================
        // TC02: Subtracting a point from itself should throw an exception.
        assertThrows(
                IllegalArgumentException.class,
                () -> point1.subtract(point1),
                "Can't subtract the same two points!"
        );
    }

    /**
     * Test the {@link Point#distanceSquared(Point)} method.
     * Ensures the squared distance between two points is computed correctly.
     */
    @Test
    public void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test the distance squared of the vector
        Point point1 = new Point(1,0,0);
        Point point2 = new Point(2,0,0);
        assertEquals(
                1,
                point1.distanceSquared(point2),
                "The squared distance should be 1"
        );
    }

    /**
     * Test the {@link Point#distance(Point)} method.
     * Ensures the actual Euclidean distance between two points is computed correctly.
     */
    @Test
    public void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test the distance of the vector
        Point point1 = new Point(1,0,0);
        Point point2 = new Point(2,0,0);
        assertEquals(
                1,
                point1.distance(point2),
                "The distance should be 1"
        );
    }
}
