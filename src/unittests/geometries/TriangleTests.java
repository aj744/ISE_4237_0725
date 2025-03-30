package unittests.geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import geometries.Triangle;
import primitives.Point;
import primitives.Vector;

import java.util.*;

public class TriangleTests {
    // ============ Equivalence Partitions Tests ==============
    // TC01: get a normal for some point (All points are the same)
    @Test
    public void TestGetNormal() {
        List<Point> points = new ArrayList<>(); // create a list of points
        points.add(new Point(0, 0, 0));
        points.add(new Point(0, 3, 0));
        points.add(new Point(0, 0, 3));

        Triangle triangle = new Triangle(points);

        assertEquals(
                new Vector(1, 0, 0),
                triangle.getNormal(new Point(0, 1, 0)),
                "the normal should be (1, 0, 0)"
        );
    }
}
