package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a point light source in a 3D scene.
 * A point light emits light uniformly in all directions from a specific position in space.
 * The intensity of the light decreases with distance using an attenuation formula.
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the light source in space.
     */
    protected final Point position;

    /**
     * Constant attenuation factor.
     */
    private double kC = 1;

    /**
     * Linear attenuation factor.
     */
    private double kL = 0;

    /**
     * Quadratic attenuation factor.
     */
    private double kQ = 0;

    /**
     * Constructs a point light source with a given position and intensity.
     *
     * @param intensity the color intensity of the light
     * @param position  the position of the light in 3D space
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC the constant attenuation coefficient
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL the linear attenuation coefficient
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ the quadratic attenuation coefficient
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Calculates the light intensity at a given point, considering attenuation.
     *
     * @param p the point in space where the intensity is calculated
     * @return the attenuated color intensity at the given point
     */
    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        return super.getIntensity().scale(1.0f / (kC + kL * d + kQ * d * d));
    }

    /**
     * Returns the normalized vector from the light source to the given point.
     *
     * @param p the point in space
     * @return the normalized direction vector from the light to the point
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }
}
