package unittests.geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

import geometries.Triangle;
import geometries.Geometries;
import geometries.Plane;
import geometries.Sphere;

/**
 * Unit tests for {@link Geometries#findIntersections(Ray)} method.
 * <p>
 * These tests verify the correctness of intersection detection between a ray and multiple geometries,
 * including various edge and boundary cases.
 */
class GeometriesTest {

    // A composite geometry containing a sphere, triangle, and plane.
    private final Geometries geometries = new Geometries(
            new Sphere(new Point(0, 0, 1), 1),
            new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0)),
            new Plane(new Point(0, 0, 3), new Vector(0, 0, 1))
    );

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}.
     * <p>
     * This method includes:
     * <ul>
     *     <li><b>TC01:</b> An empty Geometries object - expects null.</li>
     *     <li><b>TC02:</b> Ray does not intersect any geometry - expects null.</li>
     *     <li><b>TC03:</b> Ray intersects only one geometry - expects 2 intersection points (sphere).</li>
     *     <li><b>TC04:</b> Ray intersects some geometries - expects 3 points (sphere + triangle).</li>
     *     <li><b>TC05:</b> Ray intersects all geometries - expects 4 points (sphere, triangle, and plane).</li>
     * </ul>
     */
    @Test
    void testFindIntersections() {
        // TC04: some geometries are intersected
        assertEquals(3, geometries.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 1, 1))).size(), "some geometries are intersected");

        // ================= Boundary Values Tests =================
        // TC01: empty geometries list
        assertNull(new Geometries().findIntersections(
                        new Ray(new Point(1,1,1), new Vector(1,1,1))),
                "empty geometries list"
        );

        // TC02: no geometry is intersected
        assertNull(
                geometries.findIntersections(
                        new Ray(new Point(1,1,2.5),
                                new Vector(1,0,0))),
                "no geometry is intersected"
        );

        // TC03: one geometry is intersected
        assertEquals(
                2,
                geometries.findIntersections(
                        new Ray(new Point(0, -2, 1),
                                new Vector(0, 1, 0))
                ).size(),
                "one geometry is intersected"
        );

        // TC05: all geometries are intersected
        assertEquals(
                4,
                geometries.findIntersections(
                        new Ray(new Point(0.6, 0.6, -2),
                                new Vector(0, 0, 1))).size(),
                "all geometries are intersected"
        );
    }
}
