package geometries;

/**
 * Represents a radial geometry, characterized by a radius.
 * <p>
 * This is an abstract class that serves as a base for all geometric shapes with a circular cross-section.
 * </p>
 */
public abstract class RadialGeometry extends Geometry {
    /**
     * the radial shape's radius
     */
    protected final double radius;

    /**
     * Constructs a radial geometry object with a given radius.
     *
     * @param radius The radius of the geometry (must be greater than 0).
     * @throws IllegalArgumentException if the radius is zero or negative.
     */
    public RadialGeometry(double radius) {
        if (radius <= 0) { // Radius can't be negetive
            throw new IllegalArgumentException("Radius must be greater than 0"); // if so its throw an expactation
        }
        this.radius = radius; // else its set the radius
    }
}
