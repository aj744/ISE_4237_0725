package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Sphere;
import primitives.Point;
import primitives.Vector;

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
}
