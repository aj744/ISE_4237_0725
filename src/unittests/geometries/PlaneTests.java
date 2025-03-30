package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Plane;
import primitives.Vector;
import primitives.Point;

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
        Plane plane = new Plane(new Point(0, 0, 0), new Vector(1, 1, 1));

        // The normal can be either (1,1,1) normalized or its opposite (-1,-1,-1) normalized
        assertTrue(
                new Vector(1, 1, 1).normalize().equals(plane.getNormal(new Point(0, 0, 0)))
                        || new Vector(-1, -1, -1).normalize().equals(plane.getNormal(new Point(0, 0, 0))),
                "The normal should be (1,1,1) normalized or (-1,-1,-1) normalized"
        );
    }
}
