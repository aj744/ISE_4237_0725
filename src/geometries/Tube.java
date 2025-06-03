package geometries;
import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * represents a tube shape in 3D space.
 */
public class Tube extends RadialGeometry {
    /**
     * the ray's axis
     */
    protected final Ray axis;

    /**
     * A constructor for the Tube
     *
     * @param radius the tube's radius
     * @param axis the tube's axis (direction)
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;

    }

    @Override
    public Vector getNormal(Point point){
        Vector u = point.subtract(axis.getHead());
        double t = axis.getDirection().dotProduct(u);
        Point O = axis.getHead().add(axis.getDirection().scale(t));
        return point.subtract(O).normalize();
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

}
