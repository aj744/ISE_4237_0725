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

/**
 * Integration tests for {@link Camera} rays with different geometries.
 * <p>
 * These tests verify the correct number of intersection points between rays
 * constructed by the camera through the view plane and various 3D geometries
 * like spheres, planes, and triangles.
 */
public class CameraIntersectionsIntegrationTests {

    /** A reusable camera builder configured with basic parameters */
    Camera.Builder cameraBuilder = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setResolution(3, 3)
            .setVpDistance(1)
            .setDirection(Vector.AXIS_Z.scale(-1), Vector.AXIS_Y)
            .setVpSize(3, 3);

    /** A default camera built from the camera builder */
    Camera camera = cameraBuilder.build();

    /**
     * Asserts that the total number of intersections between rays constructed from
     * the camera through each pixel and a given geometry is equal to the expected value.
     *
     * @param numberOfIntersections Expected number of intersection points.
     * @param geometry              The {@link Geometry} object to intersect with.
     * @param camera                The {@link Camera} used to construct rays.
     */
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

    /**
     * Tests intersections between camera rays and various spheres.
     * <ul>
     *     <li>Small sphere in front of camera</li>
     *     <li>Large sphere covering the view plane</li>
     *     <li>Sphere intersected by only part of the rays</li>
     *     <li>Sphere containing the camera</li>
     *     <li>Sphere behind the camera</li>
     * </ul>
     */
    @Test
    void testSphereIntersection() {
        assertNumberOfIntersections(
                2,
                new Sphere(new Point(0, 0, -3), 1),
                camera
        );

        assertNumberOfIntersections(
                18,
                new Sphere(new Point(0, 0, -2.5), 2.5),
                cameraBuilder.setLocation(new Point(0, 0, 0.5)).build()
        );

        assertNumberOfIntersections(
                10,
                new Sphere(new Point(0, 0, -2), 2),
                cameraBuilder.setLocation(new Point(0, 0, 0.5)).build()
        );

        assertNumberOfIntersections(
                9,
                new Sphere(new Point(0, 0, -2), 4),
                camera
        );

        assertNumberOfIntersections(
                0,
                new Sphere(new Point(0, 0, 1), 0.5),
                camera
        );
    }

    /**
     * Tests intersections between camera rays and triangles.
     * <ul>
     *     <li>Small triangle intersecting one ray</li>
     *     <li>Large triangle intersecting two rays</li>
     * </ul>
     */
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

    /**
     * Tests intersections between camera rays and planes.
     * <ul>
     *     <li>Orthogonal plane to camera (all rays intersect)</li>
     *     <li>Sloped plane intersecting all rays</li>
     *     <li>Plane intersecting only part of the rays</li>
     * </ul>
     */
    @Test
    void testPlaneIntersection() {
        assertNumberOfIntersections(
                9,
                new Plane(new Point(0, 0, -2), new Vector(0, 0, -1)),
                camera
        );

        assertNumberOfIntersections(
                9,
                new Plane(new Point(0, 0, -7), new Vector(0, 1, -5)),
                camera
        );

        assertNumberOfIntersections(
                6,
                new Plane(new Point(0, 0, -7), new Vector(0, 5, -1)),
                camera
        );
    }
}
