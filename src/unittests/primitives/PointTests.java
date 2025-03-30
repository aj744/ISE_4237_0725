package unittests.primitives;
import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PointTests {
    @Test
    public void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Point point = new Point(1,1,1);
        Vector vector = new Vector(2,2,2);
        assertEquals(
                new Point(3,3,3),
                point.add(vector),
                "the result should be Point(3,3,3)"
        );


    }

    @Test
    public void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There are two different points
        Point point1 = new Point(1,1,1);
        Point point2 = new Point(2,2,2);
        assertEquals(
                new Vector(-1,-1,-1),
                point1.subtract(point2) ,
                "the result should be Vector(-1,-1,-1)"
        );
        // =============== Boundary Values Tests ==================
        // TC02: if the points  are the same:
        assertThrows(
                IllegalArgumentException.class,
                () -> point1.subtract(point1),
                "Can't subtract the same 2 points!"
        );
    }

    @Test
    public void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        Point point1 = new Point(1,0,0);
        Point point2 = new Point(2,0,0);
        assertEquals(
                1,
                point1.distanceSquared(point2),
                "the distance should be 1"
        );

    }

    @Test
    public void testDistance() {
        Point point1 = new Point(1,0,0);
        Point point2 = new Point(2,0,0);
        assertEquals(
                1,
                point1.distance(point2),
                "the distance should be 1"
        );
    }


}
