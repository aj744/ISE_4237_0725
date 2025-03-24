package geometries;

import primitives.Vector;
import primitives.Point;

/**
 * Represents a geometric shape in 3D space.
 * This is an abstract class that defines the basic behavior for all geometries.
 */

public abstract class Geometry {

    /**
     * Calculates and returns the normal vector to the geometry at a given point.
     * <p>
     * Since this class is abstract, specific geometries must implement this method.
     * </p>
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector at the given point.
     */
    public abstract Vector getNormal(Point point);
}
