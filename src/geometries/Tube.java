package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * Represents an infinite cylindrical tube in 3D space.
 * The tube is defined by a central axis represented as a {@link Ray}
 * and a fixed radius.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis ray of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a new Tube with a given radius and axis.
     *
     * @param radius the radius of the tube
     * @param axis the axis ray of the tube (defines direction and position)
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Calculates the normal vector to the surface of the tube at a given point.
     *
     * @param point the point on the tube surface
     * @return the normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point point){
        Vector u = point.subtract(axis.getHead());
        double t = axis.getDirection().dotProduct(u);
        Point O = axis.getHead().add(axis.getDirection().scale(t));
        return point.subtract(O).normalize();
    }

    /**
     * Calculates the intersection points of a given ray with the tube.
     * <p>Currently not implemented - returns null.</p>
     *
     * @param ray the ray to intersect with the tube
     * @return a list of intersection points (currently null)
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }
}
