package unittests.primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link Ray} class.
 * This class tests methods for point calculation along the ray
 * and finding the closest point from a list to the ray's origin.
 */
public class RayTests {

    /**
     * Tests the {@link Ray#getPoint(double)} method.
     * <p>
     * Verifies that the method correctly returns a point on the ray
     * given a scalar distance along the direction vector.
     * </p>
     * <br>
     * <b>Test cases:</b>
     * <ul>
     *     <li>Distance = 1 (positive direction)</li>
     *     <li>Distance = -1 (negative direction)</li>
     *     <li>Distance = 0 (origin point)</li>
     * </ul>
     */
    @Test
    public void testGetPoint() {
        Ray ray = new Ray(Point.ZERO, new Vector(1, 0, 0));
        assertEquals(
                new Point(1, 0, 0),
                ray.getPoint(1),
                "The point should be equals"
        );
        assertEquals(
                new Point(-1, 0, 0),
                ray.getPoint(-1),
                "The point should be equals"
        );
        assertEquals(
                Point.ZERO,
                ray.getPoint(0),
                "The point should be equals"
        );
    }

    /**
     * Tests the {@link Ray#findClosestPoint(List)} method.
     * <p>
     * Verifies that the method correctly finds the point closest to the ray's origin
     * from a list of points, covering both equivalence and boundary test cases.
     * </p>
     * <br>
     * <b>Test cases:</b>
     * <ul>
     *     <li>Middle point is the closest</li>
     *     <li>First point is the closest</li>
     *     <li>Last point is the closest</li>
     * </ul>
     */
    @Test
    public void testFindClosestPoint() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: return the middle point
        List<Point> points1 = new ArrayList<>();
        Point p1 = new Point(0, 2, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 3, 0);

        points1.add(p1);
        points1.add(p2);
        points1.add(p3);
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));

        isClosest(points1, ray, "The middle point should be the closest", p2);

        // =============== Boundary Values Tests ==================
        // BV01: the closest point is the first one
        List<Point> points3 = new ArrayList<>();
        points3.add(p2);
        points3.add(p3);
        points3.add(p1);
        isClosest(points3, ray, "The first point should be the closest", p2);

        // BV02: the closest point is the last one
        List<Point> points4 = new ArrayList<>();
        points4.add(p1);
        points4.add(p3);
        points4.add(p2);
        isClosest(points4, ray, "The last point should be the closest", p2);
    }

    /**
     * Helper method to assert the expected closest point found by the ray.
     *
     * @param points  List of {@link Point} to search in.
     * @param ray     The {@link Ray} to compare distances from.
     * @param message Message to display on assertion failure.
     * @param expected The expected closest {@link Point}.
     */
    public void isClosest(List<Point> points, Ray ray, String message, Point expected) {
        assertNotNull(
                points,
                "the list should not be null"
        );
        assertEquals(
                expected,
                ray.findClosestPoint(points),
                message
        );
    }
}
