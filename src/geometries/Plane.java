package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a plane in 3D space, defined by a point and a normal vector.
 * <p>
 * A plane can be constructed either from one point and a normal vector,
 * or from three points that define the plane uniquely.
 * </p>
 * <p>
 * Extends {@link Geometry}, and thus supports emission color, material properties,
 * and intersection calculation.
 * </p>
 */
public class Plane extends Geometry {

    /**
     * A point on the plane.
     */
    private final Point point;

    /**
     * The normal vector perpendicular to the plane.
     */
    private final Vector normal;

    /**
     * Constructs a plane using a given point and a normal vector.
     *
     * @param point  A point on the plane.
     * @param normal The normal vector of the plane (will be normalized).
     */
    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.normalize();
    }

    /**
     * Constructs a plane using three non-collinear points in space.
     * <p>
     * The normal is computed using the cross product of two edge vectors defined by the points.
     * </p>
     *
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.normal = p2.subtract(p1).crossProduct(p2.subtract(p3)).normalize(); // Compute normal vector
        this.point = p2; // Any point on the plane is valid
    }

    /**
     * Returns the normal vector of the plane.
     * <p>
     * Since the plane's normal is constant, the given point is ignored.
     * </p>
     *
     * @param point A point on the plane (unused).
     * @return The normalized normal vector of the plane.
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    /**
     * Returns the normal vector of the plane.
     *
     * @return the normalized normal vector of the plane.
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Computes the intersection point(s) between the plane and a given ray.
     *
     * @param ray the ray to intersect with the plane
     * @return a list containing a single {@link Intersection} if found, or {@code null} if there is no intersection
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {

        // Check if the ray is parallel to the plane
        double denominator = normal.dotProduct(ray.getDirection());
        if (denominator == 0) {
            return null; // The ray is parallel to the plane
        }

        // Check if the ray starts on the plane
        if (ray.getHead().equals(point)) {
            return null;
        }

        // Calculate the t parameter for the intersection point
        double t = normal.dotProduct(point.subtract(ray.getHead())) / denominator;
        if (t <= 0) {
            return null; // The intersection is behind the ray's origin or at origin
        }

        // Return the intersection point
        if (alignZero(t - maxDistance) <= 0) {
            Intersection intersection = new Intersection(this, ray.getPoint(t));
            return List.of(intersection);
        }
        else {
            return null;
        }
    }
}
