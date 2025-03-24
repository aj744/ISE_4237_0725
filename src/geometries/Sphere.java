package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 * <p>
 * This class extends {@link RadialGeometry} since a sphere is a type of radial geometry.
 * </p>
 */
public class Sphere extends RadialGeometry {
    private final Point center;

    /**
     * Constructs a Sphere with a given radius and center point.
     *
     * @param radius The radius of the sphere (must be greater than 0).
     * @param center The center point of the sphere.
     */
    public Sphere(double radius, Point center) {
        super(radius); // send to DAD ctor
        this.center = center; // and set the new fileds
    }

    /**
     * Returns the normal vector to the sphere at a given point.
     * <p>
     * This method is currently not implemented and returns {@code null}.
     * </p>
     *
     * @param point The point on the sphere's surface.
     * @return The normal vector at the given point (currently returns {@code null}).
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}

