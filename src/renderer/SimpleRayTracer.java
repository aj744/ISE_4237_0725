package renderer;

import geometries.Intersectable.Intersection;
import geometries.Polygon;
import geometries.Sphere;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import java.util.Random;

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
    private static final int GLOSSY_RAYS = 16;     // Number of rays for glossy reflections
    private static final int BLURRY_RAYS = 16;     // Number of rays for blurry refractions
    private static final double GLOSSY_RADIUS = 0.1;  // Radius for glossy effect
    private static final double BLURRY_RADIUS = 1.5;  // Radius for blurry effect

    private final Random random = new Random();

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
        if (intersection == null) {
            return scene.background;
        }
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

        return Util.alignZero(intersection.vNormal * intersection.lNormal) > 0;
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection containing all necessary data
     * @return the resulting color from local effects
     */
    private Color calcLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            Double3 ktr = transparency(intersection);
            if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K) ) { // sign(nl) == sign(nv)
                Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
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

        if (intersections == null || intersections.isEmpty()) {
            return true; // No obstructions, fully unshaded
        }

        // Check if any intersection actually blocks the light (not transparent)
        for (Intersection obstruction : intersections) {
            // If the obstructing object is not fully transparent, it blocks some light
            if (obstruction.material.kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false; // Object is opaque enough to cast shadow
            }
        }

        return true; // All obstructions are transparent enough
    }

    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    private Ray constructReflectedRay(Intersection intersection) {
        Vector r = intersection.v.add(intersection.normal.scale(intersection.vNormal * -2));
        return new Ray(intersection.point, r, intersection.normal);
    }

    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Color refractedColor = calcBlurryRefraction(intersection, level, k, intersection.material.kT);
        Color reflectedColor = calcGlossyReflection(intersection, level, k, intersection.material.kR);
        return refractedColor.add(reflectedColor);
    }

    private Color calcGlossyReflection(Intersection intersection, int level, Double3 k, Double3 kR) {
        Double3 kkR = k.product(kR);
        if (kkR.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Vector baseReflection = intersection.v.add(intersection.normal.scale(intersection.vNormal * -2));

        if (!intersection.material.isGlossy()) {
            // Regular reflection
            Ray reflectedRay = new Ray(intersection.point, baseReflection, intersection.normal);
            Intersection reflectedIntersection = findClosestIntersection(reflectedRay);
            if (reflectedIntersection == null) return scene.background.scale(kR);
            return preprocessIntersection(reflectedIntersection, reflectedRay.getDirection())
                    ? calcColor(reflectedIntersection, level - 1, kkR).scale(kR) : Color.BLACK;
        }

        // Glossy reflection
        Color totalColor = Color.BLACK;
        double glossyRadius = intersection.material.glossiness * GLOSSY_RADIUS;

        for (int i = 0; i < GLOSSY_RAYS; i++) {
            Vector glossyDirection = generateRandomDirection(baseReflection, intersection.normal, glossyRadius);
            Ray glossyRay = new Ray(intersection.point, glossyDirection, intersection.normal);

            Intersection glossyIntersection = findClosestIntersection(glossyRay);
            if (glossyIntersection != null && preprocessIntersection(glossyIntersection, glossyRay.getDirection())) {
                totalColor = totalColor.add(calcColor(glossyIntersection, level - 1, kkR));
            } else {
                totalColor = totalColor.add(scene.background);
            }
        }

        return totalColor.scale(kR).reduce(GLOSSY_RAYS);
    }

    private Color calcBlurryRefraction(Intersection intersection, int level, Double3 k, Double3 kT) {
        Double3 kkT = k.product(kT);
        if (kkT.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Vector baseRefraction = intersection.v;

        if (!intersection.material.isBlurry()) {
            // Regular refraction
            Ray refractedRay = new Ray(intersection.point, baseRefraction, intersection.normal);
            Intersection refractedIntersection = findClosestIntersection(refractedRay);
            if (refractedIntersection == null) return scene.background.scale(kT);
            return preprocessIntersection(refractedIntersection, refractedRay.getDirection())
                    ? calcColor(refractedIntersection, level - 1, kkT).scale(kT) : Color.BLACK;
        }

        // Blurry refraction
        Color totalColor = Color.BLACK;
        double blurryRadius = intersection.material.blurriness * BLURRY_RADIUS;

        for (int i = 0; i < BLURRY_RAYS; i++) {
            Vector blurryDirection = generateRandomDirection(baseRefraction, intersection.normal, blurryRadius);
            Ray blurryRay = new Ray(intersection.point, blurryDirection, intersection.normal);

            Intersection blurryIntersection = findClosestIntersection(blurryRay);
            if (blurryIntersection != null && preprocessIntersection(blurryIntersection, blurryRay.getDirection())) {
                totalColor = totalColor.add(calcColor(blurryIntersection, level - 1, kkT));
            } else {
                totalColor = totalColor.add(scene.background);
            }
        }

        return totalColor.scale(kT).reduce(BLURRY_RAYS);
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

        Double3 ktr = Double3.ONE;
        if (intersections == null) return ktr;

        for (Intersection i : intersections) {
            if (i.material.kT.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
            ktr = ktr.product(i.material.kT);
        }
        return ktr;
    }

    /**
     * Generates a random vector within a hemisphere around the given direction.
     */
    private Vector generateRandomDirection(Vector direction, Vector normal, double radius) {
        if (radius <= 0) {
            return direction;
        }

        // Create orthonormal basis
        Vector u, v;
        if (Math.abs(normal.dotProduct(Vector.AXIS_X)) > 0.9) {
            u = normal.crossProduct(new Vector(0, 1, 0)).normalize();
        } else {
            u = normal.crossProduct(new Vector(1, 0, 0)).normalize();
        }
        v = normal.crossProduct(u);

        // Generate random point in unit disk
        double r = Math.sqrt(random.nextDouble()) * radius;
        double theta = random.nextDouble() * 2 * Math.PI;
        double offsetU = r * Math.cos(theta);
        double offsetV = r * Math.sin(theta);

        // Perturb the direction
        Vector perturbedDirection = direction;
        if (offsetU != 0) perturbedDirection = perturbedDirection.add(u.scale(offsetU));
        if (offsetV != 0) perturbedDirection = perturbedDirection.add(v.scale(offsetV));

        try {
            return perturbedDirection.normalize();
        } catch (Exception e) {
            return direction;
        }
    }
}