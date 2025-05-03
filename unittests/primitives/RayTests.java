package unittests.primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

public class RayTests {
    @Test
    public void testGetPoint(){
        Ray ray = new Ray(new Vector(1, 0, 0), Point.ZERO);
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
}