package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

import java.util.List;
import java.util.ArrayList;

import static primitives.Util.alignZero;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 * <p>
 * This class extends {@link RadialGeometry} since a sphere is a type of radial geometry.
 * </p>
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a Sphere with a given center point and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere (must be greater than 0).
     */
    public Sphere(Point center, double radius) {
        super(radius); // Call superclass constructor
        this.center = center;
    }

    /**
     * Returns the normal vector to the sphere at a given point on its surface.
     * The normal is computed as the normalized vector from the sphere's center to the given point.
     *
     * @param point The point on the sphere's surface.
     * @return The normalized normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * Calculates the intersection points between the sphere and a given ray.
     *
     * @param ray The ray to test for intersection with the sphere.
     * @return A list of {@link Intersection} points if intersections exist; {@code null} otherwise.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {

        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();

        if (p0.equals(center)) {
            // The ray starts at the center of the sphere
            return List.of(
                    new Intersection(this,
                            p0.add(dir.scale(radius))));
        }

        Vector u = center.subtract(p0); // Vector from ray head to sphere center
        double tm = dir.dotProduct(u);  // Projection of u on the ray direction
        double dSquared = u.dotProduct(u) - tm * tm; // Square of distance from center to ray
        double d = Math.sqrt(dSquared);

        if (d >= radius) {
            // The ray misses the sphere
            return null;
        }

        double th = Math.sqrt(radius * radius - dSquared); // Distance from closest point to intersections
        double t1 = alignZero(tm - th); // First intersection distance from ray head
        double t2 = alignZero(tm + th); // Second intersection distance

        List<Intersection> intersections = new ArrayList<>();
        if (t1 > 0 && alignZero(t1 - maxDistance) <= 0) {
            intersections.add(new Intersection(this, ray.getPoint(t1)));
        }
        if (t2 > 0 && alignZero(t2 - maxDistance) <= 0) {
            intersections.add(new Intersection(this, ray.getPoint(t2)));
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
