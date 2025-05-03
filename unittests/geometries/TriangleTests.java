package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.*;

/**
 * Unit tests for the {@link Triangle} class.
 * This class tests the normal computation for a triangle.
 */
public class TriangleTests {

    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     * Ensures that the computed normal is correct.
     */
    @Test
    public void TestGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Compute the normal for a given point (all points lie on the same triangle)

        // Create a list of three points forming a triangle
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0, 0));
        points.add(new Point(0, 3, 0));
        points.add(new Point(0, 0, 3));

        // Create the triangle
        Triangle triangle = new Triangle(new Point(0, 0, 0),
                new Point(0, 3, 0),
                new Point(0, 0, 3));

        // Check if the normal is either (1, 0, 0) or (-1, 0, 0) due to direction ambiguity
        assertTrue(
                new Vector(1, 0, 0).equals(triangle.getNormal(new Point(0, 1, 0)))
                        || new Vector(-1, 0, 0).equals(triangle.getNormal(new Point(0, 1, 0))),
                "Triangle normal should be either (1,0,0) or (-1,0,0)"
        );
    }

    /** a point used in testing */
    final Point p100 = new Point(1, 0, 0);
    /** a point used in testing */
    final Point p001 = new Point(0, 0, 1);
    /** a vector used in testing */
    final Vector v010 = new Vector(0, -1, 0);
    @Test
    public void testFindIntersections() {
        final Triangle triangle = new Triangle(Point.ZERO, p100, p001);

        // ============ Equivalence Partitions Tests ==============
        // TC01
        final var exp1 = triangle.findIntersections(new Ray(v010, new Point(0.3, 1, 0.3)));
        assertNotNull(exp1, "there are intersections");
        assertEquals(1, exp1.size(), "wrong amount of intersections");
        assertEquals(List.of(new Point(0.3, 0, 0.3)), exp1, "wrong intersection point");

        // TC02
        assertNull(triangle.findIntersections(new Ray(v010, new Point(1, 1, 1))), "no intersection point");

        // TC03
        assertNull(triangle.findIntersections(new Ray(v010, new Point(-1, 1, -1))), "no intersection point");

        // =============== Boundary Values Tests ==================
        // TC11
        assertNull(triangle.findIntersections(new Ray(v010, new Point(0,1, 2))), "no intersection point");

        // TC12
        assertNull(triangle.findIntersections(new Ray(v010, new Point(0.5,1, 0))), "no intersection point");

        // TC13
        assertNull(triangle.findIntersections(new Ray(v010, p100)), "no intersection point");
    }
}
