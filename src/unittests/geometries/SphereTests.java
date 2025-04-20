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
 * This class tests the calculation of the normal to a sphere at a given point.
 */
public class SphereTests {

    /**
     * Test method for {@link Sphere#getNormal(Point)}.
     * Ensures that the computed normal is correct for a given point on the sphere's surface.
     */
    @Test
    public void TestGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: A point on the sphere's surface should return the correct normal
        Sphere sphere = new Sphere(1, new Point(0, 0, 0));

        // Expected normal is (1, 0, 0) for a point on the positive X-axis
        assertEquals(
                new Vector(1, 0, 0),
                sphere.getNormal(new Point(1, 0, 0)),
                "The normal should be (1, 0, 0)"
        );
    }

    /** A point used in some tests */
    private final Point p001 = new Point(0, 0, 1);
    /** A point used in some tests */
    private final Point p100 = new Point(1, 0, 0);
    /** A vector used in some tests */
    private final Vector v001 = new Vector(0, 0, 1);
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, p100);
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
        final Point p300 = new Point(3,0, 0);
        final Point p310 = new Point(3, 1, 0);
        final Vector v510 = new Vector(0.5, 1, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(v110, p01)), "Ray's line out of sphere");
        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(v310, p01));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");
        // TC03: Ray starts inside the sphere (1 point)
        final var result2 = sphere.findIntersections(new Ray(v510, p500));
        assertNotNull(result2, "Can't be empty list");
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(List.of(p110), result2, "Ray crosses sphere");
        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(v310, p300)));
        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        final var result3 = sphere.findIntersections(new Ray(v110, Point.ZERO));
        assertNotNull(result3, "Can't be empty list");
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(p110, v110), result3, "Ray crosses sphere");
        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(v310, p110)), "Ray's line out of sphere");
        // **** Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        final var result4 = sphere.findIntersections(new Ray(v100, p01));
        assertNotNull(result4, "Can't be empty list");
        assertEquals(2, result4.size(), "Wrong number of points");
        assertEquals(List.of(Point.ZERO, p200), result4, "Ray crosses sphere");
        // TC22: Ray starts at sphere and goes inside (1 points)
        final var result5 = sphere.findIntersections(new Ray(v100, Point.ZERO));
        assertNotNull(result5, "Can't be empty list");
        assertEquals(1, result5.size(), "Wrong number of points");
        assertEquals(List.of(p200), result5, "Ray crosses sphere");
        // TC23: Ray starts inside (1 points)
        final var result6 = sphere.findIntersections(new Ray(v100 , p500));
        assertNotNull(result6, "Can't be empty list");
        assertEquals(1, result6.size(), "Wrong number of points");
        assertEquals(List.of(p200), result6, "Ray crosses sphere");
        // TC24: Ray starts at the center (1 points)
        final var result7 = sphere.findIntersections(new Ray(v100, p100));
        assertNotNull(result7, "Can't be empty list");
        assertEquals(1, result7.size(), "Wrong number of points");
        assertEquals(List.of(p200), result7, "Ray crosses sphere");
        // TC25: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(v100, p200)));
        // TC26: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(v100, p300)));
        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(v100, p110)));
        // TC32: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(v100, p210)));
        // TC33: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(v100, p310)));
        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(v100, p310)));
        // TC42: Ray's starts inside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(v001, p500)));
    }
}
