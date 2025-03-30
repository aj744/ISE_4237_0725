package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Sphere;
import primitives.Point;
import primitives.Vector;

public class SphereTests {
    // ============ Equivalence Partitions Tests ==============
    // TC01: get a normal for some point (All points are the same)
    @Test
    public void TestGetNormal() {
        Sphere sphere = new Sphere(1, new Point(0, 0, 0));
        assertEquals(
                new Vector(1, 0, 0),
                sphere.getNormal(new Point(1, 0, 0)),
                "the normal should be (1, 0, 0)"
        );
    }
}
