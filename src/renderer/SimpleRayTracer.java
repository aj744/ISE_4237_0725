package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A simple implementation of a ray tracer that calculates the color
 * of a point based on ambient light and direct illumination from light sources.
 * It supports diffuse and specular lighting effects based on the Phong reflection model.
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

    /**
     * Calculates the final color at the intersection point, including ambient and local effects.
     *
     * @param intersection the closest intersection point
     * @param ray the original ray
     * @return the resulting color
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }
        return scene
                .ambientLight.getIntensity().scale(intersection.material.kA).add(calcColorLocalEffects(intersection));
    }

    /**
     * Prepares intersection data including normal and view direction.
     *
     * @param intersection the intersection point
     * @param vector the view direction
     * @return true if the intersection is valid (not orthogonal to the view direction)
     */
    public boolean preprocessIntersection(Intersection intersection, Vector vector) {
        intersection.v = vector;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.v.dotProduct(intersection.normal);

        return !isZero(intersection.vNormal);
    }

    /**
     * Sets the light source and computes the light direction and its dot product with the normal.
     *
     * @param intersection the intersection point
     * @param lightSource the light source
     * @return true if the light and view directions are not orthogonal to the surface
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.light = lightSource;
        intersection.l = intersection.light.getL(intersection.point);
        intersection.lNormal = intersection.l.dotProduct(intersection.normal);

        return !(isZero(intersection.vNormal) || isZero(intersection.lNormal));
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection containing all necessary data
     * @return the resulting color from local effects
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Material material = intersection.geometry.getMaterial();
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            if (alignZero(intersection.lNormal * intersection.vNormal) > 0) { // sign(nl) == sign(nv)
                Color iL = lightSource.getIntensity(intersection.point);
                color = color.add(
                        iL.scale(calcDiffusive(intersection)
                                .add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    /**
     * Calculates the specular reflection component using the Phong model.
     *
     * @param intersection the intersection containing vectors and material
     * @return the specular reflection coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.normal.scale(2 * intersection.lNormal));
        double vr = -1 * intersection.v.dotProduct(r);

        return intersection.material.kS.scale(Math.pow(Math.max(0, vr), intersection.material.nShininess));
    }

    /**
     * Calculates the diffuse reflection component at the intersection point
     * based on the Phong reflection model.
     *
     * @param intersection the intersection data including normal and material
     * @return the diffuse reflection as a Double3 coefficient
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }
}
