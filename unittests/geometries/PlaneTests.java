package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Plane;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.List;

/**
 * Unit tests for the {@link Plane} class.
 * <p>
 * This class includes tests for:
 * <ul>
 *     <li>{@link Plane#getNormal(Point)} - Verifying the correct calculation of the normal vector.</li>
 *     <li>{@link Plane#findIntersections(Ray)} - Checking intersection points between a ray and a plane,
 *         including various boundary and equivalence cases.</li>
 * </ul>
 */
public class PlaneTests {

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     * <p>
     * Ensures that the computed normal is correct for a given point on the plane.
     * The normal should be a unit vector perpendicular to the plane.
     */
    @Test
    public void TestGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: The normal at a point on the plane should match the plane's normal vector.
        Plane plane = new Plane(Point.ZERO, new Vector(1, 1, 1));

        // The normal can be either (1,1,1) normalized or its opposite (-1,-1,-1) normalized
        assertTrue(
                new Vector(1, 1, 1).normalize().equals(plane.getNormal(Point.ZERO))
                        || new Vector(-1, -1, -1).normalize().equals(plane.getNormal(Point.ZERO)),
                "The normal should be (1,1,1) normalized or (-1,-1,-1) normalized"
        );
    }

    final Vector v100 = new Vector(1, 0, 0);

    /**
     * Test method for {@link Plane#findIntersections(Ray)}.
     * <p>
     * Tests intersection scenarios between a ray and a plane, including:
     * <ul>
     *     <li><b>TC01:</b> Ray intersects the plane in one point.</li>
     *     <li><b>TC02:</b> Ray does not intersect the plane.</li>
     *     <li><b>TC11-TC12:</b> Ray is parallel to the plane (both not included and included in the plane).</li>
     *     <li><b>TC21-TC23:</b> Ray is orthogonal to the plane (no intersection or intersects once).</li>
     *     <li><b>TC31-TC41:</b> Ray is neither parallel nor orthogonal (no intersection).</li>
     * </ul>
     */
    @Test
    public void testFindIntersections() {

        Plane plane = new Plane(new Point(1, 2, 2), v100);

        final Point p_100 = new Point(-1, 0, 0);
        final var p122 = new Point(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: If there is an intersection
        final var exp1 = plane.findIntersections(
                new Ray(new Point(2, 0, 0),
                        new Vector(-1, -1, -1)));

        assertNotNull(exp1, "intersections is NULL");
        assertEquals(1, exp1.size(), "The intersection should be 1");
        assertEquals(List.of(new Point(1, -1, -1)), exp1, "The intersection are not equals");

        // TC02: If there is no intersection
        final Vector v111 = new Vector(0, 1, 1);
        assertNull(plane.findIntersections(new Ray(p122, v111)), "There is no intersections");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray is parallel to the plane
        // TC11: Ray is not included in the plane
        final Vector v010 = new Vector(0, 1, 0);
        assertNull(
                plane.findIntersections(new Ray(new Point(2, 2, 2), v010)),
                "is parallel"
        );

        // TC12: Ray is included in the plane
        final Point p222 = new Point(2, 2, 2);
        assertNull(
                plane.findIntersections(new Ray(p122, v010)),
                "is parallel and included"
        );

        // **** Group 2: Ray is orthogonal to the plane
        // TC21: Ray is orthogonal and starts before the plane
        assertNull(
                plane.findIntersections(new Ray(p222, v100)),
                "There is no intersections"
        );

        // TC22: Ray is orthogonal and starts after the plane
        Point p133 = new Point(1, 3, 3);
        assertNull(
                plane.findIntersections(new Ray(p133, v100)),
                "There is no intersections"
        );

        // TC23: Ray is orthogonal and starts before and intersects
        final var exp2 = plane.findIntersections(new Ray(new Point(-1, -1, -1), v100));
        assertNotNull(exp2, "intersections is not NULL");
        assertEquals(1, exp2.size(), "The intersection should be 1");
        assertEquals(List.of(new Point(1, -1, -1)), exp2, "The intersection are not equals");

        // TC31: Ray is neither orthogonal nor parallel and does not intersect
        assertNull(
                plane.findIntersections(new Ray(p122, v111)),
                "There is no intersections"
        );

        // TC41: Ray is neither orthogonal nor parallel and starts after the plane
        assertNull(
                plane.findIntersections(new Ray(p133, v111)),
                "There is no intersections"
        );
    }
}
