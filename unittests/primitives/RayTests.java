package unittests.primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

public class RayTests {
    @Test
    public void testGetPoint(){
        Ray ray = new Ray(Point.ZERO, new Vector(1, 0, 0));
        assertEquals(
                new Point(1, 0, 0),
                ray.getPoint(1),
                "The point should be equals"
        );
        assertEquals(
                new Point(-1, 0, 0),
                ray.getPoint(-1),
                "The point should be equals"
        );
        assertEquals(
                Point.ZERO,
                ray.getPoint(0),
                "The point should be equals"
        );
    }

    @Test

    public void testFindClosestPoint()
    {
        // ============ Equivalence Partitions Tests ==============
        //EP01: return the middle point
        List<Point> points1 = new ArrayList<>();
        Point p1 = new Point(0, 2, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 3, 0);

        points1.add(p1);
        points1.add(p2);
        points1.add(p3);
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));

        isClosest(points1, ray, "The middle point should be the closest", p2);

        // =============== Boundary Values Tests ==================
        // BV01: if the list is empty
        List<Point> points2 = new ArrayList<>();
        assertNull(
                ray.findClosestPoint(points2),
                "The list should be empty"
        );
        // BV02: the closest point is the first one
        List<Point> points3 = new ArrayList<>();
        points3.add(p2);
        points3.add(p3);
        points3.add(p1);
        isClosest(points3, ray, "The first point should be the closest", p2);

        // BV03: the closest point is the last one
        List<Point> points4 = new ArrayList<>();
        points4.add(p1);
        points4.add(p3);
        points4.add(p2);
        isClosest(points4, ray, "The last point should be the closest", p2);

    }
    public void isClosest(List<Point> points, Ray ray,String message , Point expected){
        assertNotNull(
                points,
                "the list should not be null"
        );
        assertEquals(
                expected,
                ray.findClosestPoint(points),
                message
        );
    }



}