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
 * This class tests the calculation of the normal to a plane at a given point.
 */
public class PlaneTests {

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     * Ensures that the computed normal is correct for a given point on the plane.
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
    @Test
    public void testFindIntersections() {

        Plane plane = new Plane(new Point(1,2,2), v100);
        /*
        intreection point - x = 0   0, 1, 1
        point - x not 0             1, 2, 2
        vector - y or z not 0       -1, -1, -1
         */

        final Point p_100 = new Point(-1, 0, 0);
        final var p122 = new Point(1,0,0);
        // ============ Equivalence Partitions Tests ==============
        // TC01:If there is intersection
        final var exp1 = plane.findIntersections(new Ray(new Point(2, 0, 0), new Vector(-1,-1,-1)));
        assertNotNull(exp1, "intersections is NULL");
        assertEquals(1, exp1.size() , "The intersection should be 1");
        assertEquals(exp1 , List.of(new Point(1,-1,-1) ), "The intersection are not equals");

        // TC02:If there is no intersections
        final Vector v111 = new Vector(1, 1, 1);
        assertNull(plane.findIntersections(new Ray(p122, v111)), "There is no intersections");


        // =============== Boundary Values Tests ==================
        // **** Group 1: If the ray is parallel to the plane
        // TC11:If the ray is not included in the plane
        final Vector v010 = new Vector(0, 1, 0);
        assertNull(plane.findIntersections(new Ray(new Point(2,2,2), v010)), "is parallel");

        //TC12: If the ray is included in the plane
        final Point p022 = new Point(0, 2, 2);
        assertNull(plane.findIntersections(new Ray(p022, v010)), "is parallel and included");

        // **** Group 2:
        // TC21:If the ray is not included in the plane
        assertNull(plane.findIntersections(new Ray(p122, v100)), "There is no intersections");

        // TC22
        Point p133 = new Point(1, 3, 3);
        assertNull(plane.findIntersections(new Ray(p133, v100)), "There is no intersections");

        // TC23
        final var exp2 = plane.findIntersections(new Ray(new Point(-1,-1,-1), v100));
        assertNotNull(exp2, "intersections is not NULL");
        assertEquals(1, exp2.size() , "The intersection should be 1");
        assertEquals(exp2 , List.of(new Point(1,-1,-1)) , "The intersection are not equals");

        // TC31
        assertNull(plane.findIntersections(new Ray(p122, v111)), "There is no intersections");

        // TC41
        assertNull(plane.findIntersections(new Ray(p133, v111)), "There is no intersections");
    }

}