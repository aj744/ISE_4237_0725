package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Triangle;
import primitives.Point;
import primitives.Vector;

import java.util.*;

/**
 * Unit tests for the {@link Triangle} class.
 * This class tests the normal computation for a triangle.
 */
public class TriangleTests {

    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     * Ensures that the computed normal is correct.
     */
    @Test
    public void TestGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Compute the normal for a given point (all points lie on the same triangle)

        // Create a list of three points forming a triangle
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0, 0));
        points.add(new Point(0, 3, 0));
        points.add(new Point(0, 0, 3));

        // Create the triangle
        Triangle triangle = new Triangle(points);

        // Check if the normal is either (1, 0, 0) or (-1, 0, 0) due to direction ambiguity
        assertTrue(
                new Vector(1, 0, 0).equals(triangle.getNormal(new Point(0, 1, 0)))
                        || new Vector(-1, 0, 0).equals(triangle.getNormal(new Point(0, 1, 0))),
                "Triangle normal should be either (1,0,0) or (-1,0,0)"
        );
    }
}
