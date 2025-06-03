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
            .setLocation(Point.ZERO)
            .setResolution(3,3)
            .setVpDistance(1)
            .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
            .setVpSize(3,3);

    Camera camera = cameraBuilder.build();

    private void assertNumberOfIntersections(int numberOfIntersections, Geometry geometry, Camera camera) {
        //for each pixel we are sending a ray and save it and sum all of it if there is Intersection.
        int OverallIntersections = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Ray ray = camera.constructRay(3, 3, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null)
                    OverallIntersections += intersections.size();
            }
        }
        //if the exp != actual , error
        assertEquals(
                numberOfIntersections,
                OverallIntersections,
                "The number of intersections should be " + numberOfIntersections
        );
    }

    @Test
    void testSphereIntersection() {
        //2 Intersection and small Sphere far from camera
        assertNumberOfIntersections(
                2,
                new Sphere(new Point(0, 0, -3), 1),
                camera
        );
        //18 Intersection and big Sphere near camera
        assertNumberOfIntersections(
                18,
                new Sphere(new Point(0, 0, -2.5), 2.5),
                cameraBuilder.setLocation(new Point(0, 0, 0.5)).build()
        );
        //10 Intersection and small Sphere near camera
        assertNumberOfIntersections(
                10,
                new Sphere(new Point(0, 0, -2), 2),
                cameraBuilder.setLocation(new Point(0, 0, 0.5)).build()
        );
        //9 Intersection and big Sphere cover camera
        assertNumberOfIntersections(
                9,
                new Sphere(new Point(0, 0, -2), 4),
                camera
        );
        //0 Intersection and small Sphere behind camera
        assertNumberOfIntersections(
                0,
                new Sphere(new Point(0, 0, 1), 0.5),
                camera
        );
    }
    @Test
    void testTriangleIntersection() {
        //1 Intersection and small triangle near camera
        assertNumberOfIntersections(
                1,
                new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)),
                camera
        );

        //2 Intersection and big triangle near camera
        assertNumberOfIntersections(
                2,
                new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)),
                camera
        );
    }
    @Test
    void testPlaneIntersection(){
        //9 Intersection and orthogonal plane parallel to camera
        assertNumberOfIntersections(
                9,
                new Plane(new Point(0,0,-2) , new Vector(0,0,-1)),
                camera
        );
        //9 Intersection and plane near camera
        assertNumberOfIntersections(
                9,
                new Plane(new Point(0,0,-7) , new Vector(0,1,-5)),
                camera
        );
        //6 Intersection and plane near camera and parallel to some rays
        assertNumberOfIntersections(
                6,
                new Plane(new Point(0,0,-7) , new Vector(0,5,-1)),
                camera
        );
    }
}
