package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Sphere;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Unit tests for the {@link Sphere} class.
 * This class includes tests for the {@code getNormal} and {@code findIntersections} methods.
 */
public class SphereTests {

    /**
     * Tests the {@link Sphere#getNormal(Point)} method.
     * <p>
     * Verifies that the normal vector returned for a point on the surface
     * of the sphere is correct in direction and magnitude.
     * </p>
     * <br>
     * <b>Test case:</b><br>
     * A point on the positive X-axis of a unit sphere centered at the origin.
     */
    @Test
    public void TestGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: A point on the sphere's surface should return the correct normal
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1);

        // Expected normal is (1, 0, 0) for a point on the positive X-axis
        assertEquals(
                new Vector(1, 0, 0),
                sphere.getNormal(new Point(1, 0, 0)),
                "The normal should be (1, 0, 0)"
        );
    }

    /** A point (0, 0, 1) used in intersection tests */
    private final Point p001 = new Point(0, 0, 1);
    /** A point (1, 0, 0) used in intersection tests */
    private final Point p100 = new Point(1, 0, 0);
    /** A vector (0, 0, 1) used in intersection tests */
    private final Vector v001 = new Vector(0, 0, 1);

    /**
     * Tests the {@link Sphere#findIntersections(Ray)} method.
     * <p>
     * Verifies that the method correctly computes intersections of rays with a sphere,
     * including edge cases like tangents, orthogonal rays, rays from inside the sphere,
     * rays passing through the center, etc.
     * </p>
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);

        // Define expected intersection points and vectors for test cases
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);

        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);
        final Point p210 = new Point(2, 1, 0);
        final Point p110 = new Point(1, 1, 0);
        final Vector v100 = new Vector(1, 0, 0);
        final Point p200 = new Point(2, 0, 0);
        final Point p500 = new Point(0.5, 0, 0);
        final Point p300 = new Point(3, 0, 0);
        final Point p310 = new Point(3, 1, 0);
        final Vector v510 = new Vector(0.5, 1, 0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(
                sphere.findIntersections(new Ray(p01, v110)),
                "Ray's line out of sphere"
        );

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        final var result2 = sphere.findIntersections(new Ray(p500, v510));
        assertNotNull(result2, "Can't be empty list");
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(List.of(p110), result2, "Ray crosses sphere");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p300, v310)));

        // =============== Boundary Values Tests ==================

        // Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        final var result3 = sphere.findIntersections(new Ray(Point.ZERO, v110));
        assertNotNull(result3, "Can't be empty list");
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(p110), result3, "Ray crosses sphere");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p110, v310)), "Ray's line out of sphere");

        // Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        final var result4 = sphere.findIntersections(new Ray(p01, v100));
        assertNotNull(result4, "Can't be empty list");
        assertEquals(2, result4.size(), "Wrong number of points");
        assertEquals(List.of(Point.ZERO, p200), result4, "Ray crosses sphere");

        // TC22: Ray starts at sphere and goes inside (1 point)
        final var result5 = sphere.findIntersections(new Ray(Point.ZERO, v100));
        assertNotNull(result5, "Can't be empty list");
        assertEquals(1, result5.size(), "Wrong number of points ");
        assertEquals(List.of(p200), result5, "Ray crosses sphere");

        // TC23: Ray starts inside (1 point)
        final var result6 = sphere.findIntersections(new Ray(p500, v100));
        assertNotNull(result6, "Can't be empty list");
        assertEquals(1, result6.size(), "Wrong number of points");
        assertEquals(List.of(p200), result6, "Ray crosses sphere");

        // TC24: Ray starts at the center (1 point)
        final var result7 = sphere.findIntersections(new Ray(p100, v100));
        assertNotNull(result7, "Can't be empty list");
        assertEquals(1, result7.size(), "Wrong number of points");
        assertEquals(List.of(p200), result7, "Ray crosses sphere");

        // TC25: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p200, v100)));

        // TC26: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p300, v100)));

        // Group 3: Ray's line is tangent to the sphere (0 points)
        // TC31: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(p110, v100)));

        // TC32: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(p210, v100)));

        // TC33: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(p310, v100)));

        // Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to start-center line
        assertNull(sphere.findIntersections(new Ray(p310, v100)));

        // TC42: Ray starts inside, ray is orthogonal to start-center line
        final var result8 = sphere.findIntersections(new Ray(p500, v001));
        assertNotNull(result8, "Can't be empty list");
    }
}
