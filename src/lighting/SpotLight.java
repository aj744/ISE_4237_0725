package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a spotlight in a 3D scene.
 * A spotlight is a type of point light that has a specific direction,
 * meaning it emits light in a cone-shaped beam rather than uniformly in all directions.
 * The intensity of the light also depends on the angle between the direction of the beam
 * and the direction to the illuminated point.
 */
public class SpotLight extends PointLight {
    /**
     * The direction vector of the spotlight beam.
     */
    private final Vector direction;

    /**
     * The beam narrowing factor. A higher value results in a narrower and sharper beam.
     */
    private Double narrowBeam = 1d;

    /**
     * Constructs a new spotlight with the specified intensity, position, and direction.
     *
     * @param intensity the color intensity of the light
     * @param position  the position of the light in space
     * @param direction the direction of the spotlight beam
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the narrow beam factor of the spotlight.
     * A higher value creates a more focused beam.
     *
     * @param narrowBeam the beam narrowing factor
     * @return the current SpotLight instance (for method chaining)
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    /**
     * Sets the constant attenuation factor for the spotlight.
     *
     * @param kC the constant attenuation coefficient
     * @return the current SpotLight instance (for method chaining)
     */
    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    /**
     * Sets the linear attenuation factor for the spotlight.
     *
     * @param kL the linear attenuation coefficient
     * @return the current SpotLight instance (for method chaining)
     */
    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    /**
     * Sets the quadratic attenuation factor for the spotlight.
     *
     * @param kQ the quadratic attenuation coefficient
     * @return the current SpotLight instance (for method chaining)
     */
    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }

    /**
     * Calculates the intensity of the spotlight at a given point.
     * The intensity is attenuated by both distance and angle between the light direction and the point.
     *
     * @param point the point in space
     * @return the calculated color intensity at the given point
     */
    @Override
    public Color getIntensity(Point point) {
        Color oldColor = super.getIntensity(point);
        if (narrowBeam != 1d)
            return oldColor.scale(Math.pow(Math.max(0d, direction.dotProduct(getL(point))), narrowBeam));
        return oldColor.scale(Math.max(0d, direction.dotProduct(getL(point))));
    }
}
