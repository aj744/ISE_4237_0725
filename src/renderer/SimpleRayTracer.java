package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

/**
 * A simple implementation of a ray tracer that calculates the color
 * of a point based only on ambient light and closest intersection point.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a new SimpleRayTracer using the provided scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray and calculates the color at the intersection point,
     * or returns the background color if no intersection is found.
     *
     * @param ray the ray to be traced
     * @return the color at the closest intersection point, or the background color
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null) {
            return scene.background;
        }
        return calcColor(ray.findClosestPoint(intersections));
    }

    /**
     * Calculates the color at a given point. Currently returns only ambient light intensity.
     *
     * @param point the point at which to calculate the color
     * @return the color at the point (ambient light only)
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }

    public boolean preprocessIntersection(Intersection intersection, Vector ray) {

    }
}
