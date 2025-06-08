package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source in a 3D scene.
 * Implementations of this interface define how light interacts
 * with objects at specific points in space.
 */
public interface LightSource {
    /**
     * Returns the light intensity (color) at a given point in space.
     *
     * @param p the point at which the intensity is requested
     * @return the color intensity at the specified point
     */
    Color getIntensity(Point p);

    /**
     * Returns the normalized direction vector from the light source to the given point.
     *
     * @param p the point for which the direction vector is computed
     * @return the normalized direction vector from the light to the point
     */
    Vector getL(Point p);

    double getDistance(Point point);

}
