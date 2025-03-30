package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Tube;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * Unit tests for the {@link Tube} class.
 * This class verifies the correctness of the normal computation for a tube.
 */
public class TubeTests {

    /**
     * Test method for {@link Tube#getNormal(Point)}.
     * Ensures that the computed normal is correct in both regular and boundary cases.
     */
    @Test
    public void TestGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: A point that is not directly aligned with the tube's axis
        Tube tube = new Tube(1, new Ray(new Vector(1, 0, 0), new Point(0, 0, 0)));

        // Expected normal is (0, 1, 0)
        assertEquals(
                new Vector(0, 1, 0),
                tube.getNormal(new Point(1, 1, 0)),
                "The normal should be (0, 1, 0)"
        );

        // =============== Boundary Values Tests ==================
        // TC11: A point that is directly aligned with the tube's axis (edge case)
        assertThrows(
                IllegalArgumentException.class,
                () -> tube.getNormal(new Point(0, 1, 0)),
                "IllegalArgumentException should be thrown for points on the tube's axis"
        );
    }
}
