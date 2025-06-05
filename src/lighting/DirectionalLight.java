package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source.
 * A directional light has a specific direction but no position,
 * meaning its rays are parallel and come from infinitely far away.
 * It is similar to sunlight in rendering contexts.
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The direction vector of the light.
     */
    private final Vector direction;

    /**
     * Constructs a directional light with a specified color intensity and direction.
     *
     * @param intensity the color intensity of the light
     * @param direction the direction vector of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction;
    }

    /**
     * Returns the light intensity at a given point.
     * Since this is a directional light, the intensity is constant and
     * does not depend on the point.
     *
     * @param p the point at which the intensity is requested
     * @return the intensity of the light
     */
    @Override
    public Color getIntensity(Point p) {
        return super.getIntensity();
    }

    /**
     * Returns the normalized direction vector from the light to a given point.
     * For directional light, this vector is constant and represents the direction
     * of the light rays (opposite of where they come from).
     *
     * @param p the point to which the light direction is computed
     * @return the normalized direction vector of the light
     */
    @Override
    public Vector getL(Point p) {
        return direction.normalize();
    }
}
