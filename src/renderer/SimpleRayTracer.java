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

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

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
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return scene.background;
        }
        return calcColor(intersection, ray);
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
        return calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity().scale(intersection.material.kA));
    }

    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
    }


    /**
     * Prepares intersection data including normal and view direction.
     *
     * @param intersection the intersection point
     * @param vector       the view direction
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
     * @param lightSource  the light source
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
    private Color calcLocalEffects(Intersection intersection, Double3 k) {
        Material material = intersection.geometry.getMaterial();
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            if (alignZero(intersection.lNormal * intersection.vNormal) > 0 && unshaded(intersection)
                    && !k.lowerThan(MIN_CALC_COLOR_K)) { // sign(nl) == sign(nv)
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

    /**
     * Checks whether the point is not blocked from the light source (i.e., not in shadow).
     *
     * @param intersection the intersection to check
     * @return true if unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Ray lightRay = new Ray(intersection.point, pointToLight, intersection.normal);
        var intersections =
                scene.geometries.calculateIntersections(lightRay, intersection.light.getDistance(intersection.point));
        return intersections == null || intersections.isEmpty();
    }

    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    private Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.v;
        Vector n = intersection.normal;
        double vn = v.dotProduct(n);
        return new Ray(intersection.point, v.subtract(n.scale(2 * vn)));
    }

    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcColorGlobalEffect(constructRefractedRay(intersection),
                level, k, intersection.material.kT)
                .add(calcColorGlobalEffect(constructReflectedRay(intersection),
                        level, k, intersection.material.kR));
    }
    private Color calcColorGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx) : Color.BLACK;
    }

    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

    /**
     * Calculates the transparency factor along the path from the point to the light source.
     *
     * @param intersection the intersection point
     * @return the transparency coefficient (ktr)
     */
    private Double3 transparency(Intersection intersection) {
        Ray lightRay = new Ray(intersection.point, intersection.l.scale(-1), intersection.normal);
        List<Intersection> intersections =
                scene.geometries.calculateIntersections(lightRay, intersection.light.getDistance(intersection.point));
        if (intersections == null) return Double3.ONE;

        Double3 ktr = Double3.ONE;
        for (Intersection i : intersections) {
            ktr = ktr.product(i.geometry.getMaterial().kT);

            // If the intensity of the light ray is too small, the object is opaque
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
        }
        return ktr;
    }
}