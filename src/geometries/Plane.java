package geometries;

import primitives.*;

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
}

