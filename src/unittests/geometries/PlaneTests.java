package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Plane;
import primitives.Vector;
import primitives.Point;


public class PlaneTests {
    // ============ Equivalence Partitions Tests ==============
    // TC01: get a normal for some point (All points are the same)
    @Test
    public void TestGetNormal() {
        Plane plane = new Plane(new Point(0, 0, 0), new Vector(1, 1, 1));
        assertEquals(
                new Vector(1, 1, 1).normalize(),
                plane.getNormal(new Point(0, 0, 0)),
                "The normal should be (1, 1, 1)"
        );
    }
}
