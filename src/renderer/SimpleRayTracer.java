package renderer;

import geometries.Intersectable.Intersection;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import primitives.Material;

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
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null) {
            return scene.background;
        }
        return calcColor(ray.findClosestIntersection(intersections), ray);
    }




    private Color calcColor(Intersection intersection , Ray ray) {
        return scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().Ka);
    }
}