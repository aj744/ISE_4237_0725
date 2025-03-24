package geometries;
import primitives.Ray;
import primitives.Point;
import primitives.Vector;

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
        return null;
    }
}
