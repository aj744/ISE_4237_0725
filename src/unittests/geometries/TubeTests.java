package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Tube;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

public class TubeTests {
    @Test
    public void TestGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: a point which isn't besides the Tube's ray's point
        Tube tube = new Tube(1, new Ray(new Vector(1, 0, 0), new Point(0, 0, 0)));
        assertEquals(
            new Vector(1, 0, 0),
            tube.getNormal(new Point(1, 1, 0)),
            "the normal should be (1, 0, 0)"
        );

        // =============== Boundary Values Tests ==================
        // TC11: a point which is besides the Tube's ray's point
        assertThrows(
                IllegalArgumentException.class,
                () -> tube.getNormal(new Point(0, 1, 0)),
                "IllegalArgumentException should be thrown"
        );
    }
}
