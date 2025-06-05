package geometries;

import primitives.*;

import java.util.List;

/**
 * Represents a plane in 3D space, defined by a point and a normal vector.
 */
public class Plane extends Geometry {
    /**
     * the plane's point
     */
    private final Point point;

    /**
     * the normal vector to the plane
     */
    private final Vector normal;

    /**
     * Constructs a plane using a given point and a normal vector.
     *
     * @param point  A point on the plane.
     * @param normal The normal vector of the plane.
     */
    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.normalize();
    }

    /**
     * Constructs a plane using three points in space.
     * <p>
     * The normal is calculated using the cross product of two edge vectors.
     * </p>
     *
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.normal = p2.subtract(p1).crossProduct(p2.subtract(p3)).normalize(); // get the normal with cross product
        this.point = p2;
    }

    /**
     * Returns the normal vector of the plane.
     * <p>
     * Since a plane has a constant normal, the parameter is not used.
     * </p>
     *
     * @param point A point on the plane (not used in this implementation).
     * @return The normal vector of the plane.
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    public Vector getNormal() {
        return normal;
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {

        // Check if the ray is parallel to the plane
        double denominator = normal.dotProduct(ray.getDirection());
        if (denominator == 0) {
            return null; // The ray is parallel to the plane
        }

        if(ray.getHead().equals(point)) {
            return null; // The ray starts on the plane
        }
        // Calculate the t value for the intersection point
        double t = normal.dotProduct(point.subtract(ray.getHead())) / denominator;
        if (t <= 0) {
            return null; // The intersection point is behind the ray's origin
        }
        // Calculate the intersection point
        Intersection intersection = new Intersection(this, ray.getPoint(t));
        return List.of(intersection);
    }
}

