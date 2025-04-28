package unittests.geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import geometries.Triangle;
import geometries.Geometries;
import geometries.Plane;
import geometries.Sphere;

class GeometriesTest {

    private final Geometries geometries = new Geometries(new Sphere(1, new Point(0, 0, 1)),
            new Triangle(List.of(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0))),
            new Plane(new Point(0, 0, 3), new Vector(0, 0, 1)));

    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // TC04: some geometries are intersected
        assertEquals(3, geometries.findIntersections(new Ray(new Vector(0, 1, 1), new Point(0, -2, 0))).size(), "some geometries are intersected");

        // ================= Boundary Values Tests =================
        // TC01: empty geometries list
        assertNull(new Geometries().findIntersections(new Ray(new Vector(1,1,1), new Point(1,1,1))), "empty geometries list");

        // TC02: no geometry is intersected
        assertNull(geometries.findIntersections(new Ray(new Vector(1,0,0), new Point(1,1,2.5))), "no geometry is intersected");

        // TC03: one geometry is intersected
        assertEquals(2, geometries.findIntersections(new Ray(new Vector(0, 1, 0), new Point(0, -2, 1))).size(), "one geometry is intersected");

        // TC05: all geometries are intersected
        assertEquals(4, geometries.findIntersections(new Ray(new Vector(0, 0, 1), new Point(0.6, 0.6, -2))).size(), "all geometries are intersected");
    }
}