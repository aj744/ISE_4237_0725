package renderer;


import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraIntersectionsIntegrationTests {
    Camera.Builder cameraBuilder = Camera.getBuilder()
            .setResolution(3,3)
            .setVpDistance(1)
            .setDirection(Vector.AXIS_Y, Vector.AXIS_Z.scale(-1))
            .setVpSize(3,3);

    Camera camera = cameraBuilder.build();

    private void assertNumberOfIntersections(int numberOfIntersections, Geometry geometry, Camera camera) {
        int OverallIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Ray ray = camera.constructRay(3, 3, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null)
                    OverallIntersections += intersections.size();
            }
        }
        assertEquals(
                numberOfIntersections,
                OverallIntersections,
                "The number of intersections should be " + numberOfIntersections
        );
    }

    @Test
    void testSphereIntersection() {
        assertNumberOfIntersections(
                2,
                new Sphere(1, new Point(0, 0, -3)),
                camera
        );
        assertNumberOfIntersections(
                18,
                new Sphere(2.5, new Point(0, 0, -2.5)),
                cameraBuilder.setLocation(new Point(0, 0, 0.5)).build()
        );
        assertNumberOfIntersections(
                10,
                new Sphere(2, new Point(0, 0, -2)),
                cameraBuilder.setLocation(new Point(0, 0, 0.5)).build()
        );
        assertNumberOfIntersections(
                9,
                new Sphere(4, new Point(0, 0, -2)),
                camera
        );
        assertNumberOfIntersections(
                0,
                new Sphere(0.5, new Point(0, 0, 1)),
                camera
        );
    }
    @Test
    void testTriangleIntersection() {
        assertNumberOfIntersections(
                1,
                new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)),
                camera
        );

        assertNumberOfIntersections(
                2,
                new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)),
                camera
        );
    }
    @Test
    void testPlaneIntersection(){
        assertNumberOfIntersections(
                9,
                new Plane(new Point(0,0,-2) , new Vector(0,0,-1)),
                camera
        );
        assertNumberOfIntersections(
                9,
                new Plane(new Point(0,0,-7) , new Vector(0,1,-5)),
                camera
        );
        assertNumberOfIntersections(
                6,
                new Plane(new Point(0,0,-7) , new Vector(0,5,-1)),
                camera
        );
    }
}
