package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a cylinder in 3D space, which extends a tube by adding a height parameter.
 */
public class Cylinder extends Tube {
    /**
     * the tube's height
     */
    private final double height;

    /**
     * Constructs a new Cylinder with a given axis, radius, and height.
     *
     * @param axis   The central axis of the cylinder, represented as a Ray.
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder (must be positive).
     * @throws IllegalArgumentException if the height is negative.
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(radius, axis);
        if (height < 0) {  // hight can't be negetive
            throw new IllegalArgumentException("Height must be positive"); // if so , throw an expaction
        }
        this.height = height; // if the hight is legal , so set the hight.
    }

    /**
     * Returns the normal vector to the cylinder at a given point.
     * <p>
     * This method is currently not implemented and returns {@code null}.
     * </p>
     *
     * @param p The point on the surface of the cylinder.
     * @return The normal vector at the given point (currently returns {@code null}).
     */
    @Override
    public Vector getNormal(Point p) {
        return null; // At the begging it have to be null (instructions)
    }
}
