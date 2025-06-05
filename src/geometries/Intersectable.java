package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all objects that can be intersected by rays.
 * <p>
 * Provides an interface for calculating intersection points and includes
 * a nested {@link Intersection} class to represent detailed intersection data.
 */
public abstract class Intersectable {

    /**
     * Represents a detailed intersection between a ray and a geometry.
     * <p>
     * Includes the intersected geometry, the intersection point, and optional
     * additional data such as normal vectors, direction vectors, and lighting vectors
     * for use in lighting and shading calculations.
     */
    public static class Intersection {
        /**
         * The geometry object that was intersected.
         */
        public final Geometry geometry;

        /**
         * The point where the ray intersects the geometry.
         */
        public final Point point;

        /**
         * The material at the intersection point.
         */
        public final Material material;

        /**
         * The normal vector at the intersection point (can be set after intersection is created).
         */
        public Vector normal;

        /**
         * The direction vector of the incoming ray (can be set later).
         */
        public Vector v;

        /**
         * Dot product between {@code v} and the normal (used in lighting).
         */
        public double vNormal;

        /**
         * The light source relevant to this intersection (optional).
         */
        public LightSource light;

        /**
         * Vector from the point to the light source (used for shading).
         */
        public Vector l;

        /**
         * Dot product between {@code l} and the normal (used in lighting).
         */
        public double lNormal;

        /**
         * Constructs an intersection record with the given geometry and point.
         *
         * @param geometry the geometry that was intersected
         * @param point    the intersection point on the geometry
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry != null ? geometry.getMaterial() : null;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Intersection intersection)) return false;
            return geometry == ((Intersection) o).geometry && Objects.equals(o, this.point);
        }

        @Override
        public String toString() {
            return "Geometry: " + geometry + point.toString();
        }
    }

    /**
     * Finds all the intersection points (as {@link Point} objects) between a ray and the object.
     *
     * @param ray the ray that intersects the object
     * @return a list of intersection points, or {@code null} if no intersections are found
     */
    public List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null
                : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Calculates all intersection data between the ray and the object.
     *
     * @param ray the ray to intersect with
     * @return a list of {@link Intersection} records, or {@code null} if no intersections
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Abstract method to be implemented by subclasses to compute intersections.
     * <p>
     * Should return a list of {@link Intersection} objects that include the
     * geometry and intersection point.
     *
     * @param ray the ray to intersect with
     * @return a list of intersections, or {@code null} if none are found
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);
}
